package lost.test.quarkus.controller;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import lost.test.quarkus.MyError;
import lost.test.quarkus.common.Result;
import lost.test.quarkus.entity.Fighter;
import lost.test.quarkus.mapper.FighterMapper;
import lost.test.quarkus.model.FighterCreateParam;
import lost.test.quarkus.model.FighterEditParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.time.ZonedDateTime.now;
import static lost.test.quarkus.common.Result.ok;

@Tag(name = "Fighter API")
@Path("/fighter")
public class FighterController {
    @Operation(summary = "查询所有 fighter")
    @GET
    @Path("")
    @Produces(APPLICATION_JSON)
    public Result<List<Fighter>> findAllFighter() {
        return ok(Fighter.findAll().list());
    }

    @Operation(summary = "查询一个 fighter, by name")
    @GET
    @Path("/{name}")
    @Produces(APPLICATION_JSON)
    public Result<Fighter> findFighterByName(@PathParam("name") String name) {
        return ok(Fighter.findOneByName(name).orElse(null));
    }

    @Operation(summary = "新增一个 fighter")
    @POST
    @Path("")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public Result<Fighter> createFighter(@Valid FighterCreateParam fighterCreateParam) {
        var fighterInsert = FighterMapper.instance.fromFighterCreate(fighterCreateParam);
        fighterInsert.createdAt = now();
        fighterInsert.persistAndFlush();
        return ok(fighterInsert);
    }

    @Operation(summary = "编辑一个 fighter")
    @PUT
    @Path("")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public Result<Fighter> editFighter(@Valid FighterEditParam fighterEditParam) {
        var fighterUpdate = Fighter.findOneByName(fighterEditParam.name())
            .orElseThrow(() -> new MyError("Fighter不存在"));
        fighterUpdate.skill = fighterEditParam.skill();
        fighterUpdate.updatedAt = now();
        fighterUpdate.persistAndFlush();
        return ok(fighterUpdate);
    }

    @Operation(summary = "删除一个 fighter")
    @DELETE
    @Path("/{name}")
    @Produces(APPLICATION_JSON)
    @Transactional
    public Result<Boolean> deleteFighter(@PathParam("name") String name) {
        var found = Fighter.findOneByName(name);
        found.ifPresent(PanacheEntityBase::delete);
        return ok(true);
    }
}
