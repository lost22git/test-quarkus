package lost.test.quarkus.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import lost.test.quarkus.MyError;
import lost.test.quarkus.common.Ranges;
import lost.test.quarkus.common.Result;
import lost.test.quarkus.entity.Fighter;
import lost.test.quarkus.entity.Match;
import lost.test.quarkus.entity.MatchFighter;
import lost.test.quarkus.entity.MatchRound;
import lost.test.quarkus.model.MatchCreateParam;
import lost.test.quarkus.model.MatchEditParam;
import lost.test.quarkus.model.MatchRoundCreateParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.time.ZonedDateTime.now;
import static lost.test.quarkus.common.Result.ok;

@Tag(name = "Match API")
@Path("/match")
public class MatchController {
    @Operation(summary = "新增一个 match")
    @POST
    @Path("")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public Result<Match> createMatch(@Valid MatchCreateParam matchCreateParam) {
        var timeRange = matchCreateParam.timeRange();
        var fighterIds = matchCreateParam.fighterIds();

        var foundFighters = Fighter.find("where id in (?1)", fighterIds).list();
        if (foundFighters.isEmpty())
            throw new MyError("fighter不存在");

        var match = new Match();
        var matchFighters = foundFighters.stream().map(v -> {
            var matchFighter = new MatchFighter();
            matchFighter.fighter = (Fighter) v;
            matchFighter.timeRange = timeRange;
            matchFighter.match = match;
            return matchFighter;
        }).collect(Collectors.toSet());
        match.matchFighters = matchFighters;
        match.timeRange = timeRange;
        match.createdAt = now();
        match.persistAndFlush();

        return ok(match);
    }

    @Operation(summary = "删除一个 match")
    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @Transactional
    public Result<Boolean> deleteMatch(@PathParam("id") long id) {
        return ok(Match.deleteById(id));
    }

    @Operation(summary = "查询一个 match")
    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Result<Match> getMatch(@PathParam("id") long id) {
        return ok(Match.findById(id));
    }

    @Operation(summary = "查询所有 match")
    @GET
    @Path("")
    @Produces(APPLICATION_JSON)
    public Result<List<Match>> findAllMatches() {
        return ok(Match.listAll());
    }

    @Operation(summary = "添加 round")
    @POST
    @Path("/round")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public Result<MatchRound> createRound(
        @Valid MatchRoundCreateParam matchRoundCreateParam) {

        var matchId = matchRoundCreateParam.matchId();
        var timeRange = matchRoundCreateParam.timeRange();

        var match = (Match) Match.findById(matchId);
        if (match == null) throw new MyError("match不存在");

        if (!match.timeRange.contains(timeRange))
            throw new MyError("round时间范围%s不在match时间范围%s内".formatted(timeRange, match.timeRange));

        var matchRound = new MatchRound();
        matchRound.match = match;
        matchRound.timeRange = timeRange;
        matchRound.persistAndFlush();

        return ok(matchRound);
    }

    @Operation(summary = "删除一个 round")
    @DELETE
    @Path("/round/{id}")
    @Produces(APPLICATION_JSON)
    @Transactional
    public Result<Boolean> deleteRound(@PathParam("id") long id) {
        return ok(MatchRound.deleteById(id));
    }

    @Operation(summary = "查询一个 round")
    @GET
    @Path("/round/{id}")
    @Produces(APPLICATION_JSON)
    public Result<MatchRound> getRound(@PathParam("id") long id) {
        return ok(MatchRound.findById(id));
    }

    @Operation(summary = "编辑一个 match")
    @PUT
    @Path("")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public Result<Match> editMatch(@Valid MatchEditParam matchEditParam) {
        var matchId = matchEditParam.matchId();
        var fighterIds = matchEditParam.fighterIds();
        var timeRange = matchEditParam.timeRange();

        // 检查添加的 fighterIds 是否有效
        var foundFighters = Fighter.find("where id in (?1)", fighterIds).list()
            .stream().map(Fighter.class::cast).toList();
        if (foundFighters.isEmpty())
            throw new MyError("fighters不存在");

        // 检查 matchId 是否有效
        var foundMatch = (Match) Match.findById(matchId);
        if (foundMatch == null) throw new MyError("match不存在");

        // 检查 timeRange 是否有效
        var unionRange = foundMatch.matchRounds.stream().map(v -> v.timeRange).reduce(Ranges::union);
        if (unionRange.isPresent() && !timeRange.contains(unionRange.get()))
            throw new MyError("timeRange范围小于已存在的MatchRound.timeRange");

        var addMatchFighters = foundFighters.stream().map(v -> {
            var matchFighter = new MatchFighter();
            matchFighter.timeRange = timeRange;
            matchFighter.match = foundMatch;
            matchFighter.fighter = v;
            return matchFighter;
        }).collect(Collectors.toSet());

        foundMatch.matchFighters.retainAll(addMatchFighters);
        foundMatch.matchFighters.forEach(v -> v.timeRange = timeRange);
        foundMatch.matchFighters.addAll(addMatchFighters);
        foundMatch.timeRange = timeRange;
        foundMatch.updatedAt = now();
        foundMatch.persistAndFlush();

        return ok(foundMatch);
    }
}
