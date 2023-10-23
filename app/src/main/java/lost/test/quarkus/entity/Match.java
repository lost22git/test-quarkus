package lost.test.quarkus.entity;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "比赛")
@RegisterForReflection
@Entity
@Table(
    name = "match"
)
public class Match extends PanacheEntity {

    @Schema(description = "参赛选手列表")
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "match")
    public Set<MatchFighter> matchFighters;


    @Schema(description = "回合列表")
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "match")
    public Set<MatchRound> matchRounds;

    @Schema(description = "时间范围")
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    public Range<ZonedDateTime> timeRange;


    @Schema(description = "创建时间")
    @Column(nullable = false)
    public ZonedDateTime createdAt;

    @Schema(description = "最近更新时间")
    public ZonedDateTime updatedAt;

    public Match setFighters(List<Fighter> fighters,
                             Range<ZonedDateTime> timeRange) {
        var matchFighters = fighters.stream().map(v -> {
            var matchFighter = new MatchFighter();
            matchFighter.fighter = v;
            matchFighter.timeRange = timeRange;
            matchFighter.match = this;
            return matchFighter;
        }).collect(Collectors.toSet());
        this.matchFighters = matchFighters;
        return this;
    }

    public Match mergeFighters(List<Fighter> foundFighters,
                               Range<ZonedDateTime> timeRange) {
        var addMatchFighters = foundFighters.stream().map(v -> {
            var matchFighter = new MatchFighter();
            matchFighter.timeRange = timeRange;
            matchFighter.match = this;
            matchFighter.fighter = v;
            return matchFighter;
        }).collect(Collectors.toSet());

        this.matchFighters.retainAll(addMatchFighters);
        this.matchFighters.forEach(v -> v.timeRange = timeRange);
        this.matchFighters.addAll(addMatchFighters);

        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Match match = (Match) object;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
