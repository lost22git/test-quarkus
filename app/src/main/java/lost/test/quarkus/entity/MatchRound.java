package lost.test.quarkus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Schema(title = "MatchRound")
@RegisterForReflection
@Entity
@Table(name = "match_round")
public class MatchRound extends PanacheEntity {
    @JsonIgnore
    @Schema(title = "比赛")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id", nullable = false, updatable = false)
    public Match match;

    @Schema(title = "回合选手数据")
    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "matchRound")
    public Set<MatchRoundFighter> matchRoundFighters;


    @Schema(title = "时间范围", implementation = String.class, example = "[2020-02-02T00:00:00Z,2022-02-02T00:00:00Z]")
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    public Range<ZonedDateTime> timeRange;

    public MatchRound setFighters(Set<Long> fighterIds) {
        this.matchRoundFighters = fighterIds.stream().map(v -> {
            var matchRoundFighter = new MatchRoundFighter();
            matchRoundFighter.matchRound = this;
            matchRoundFighter.fighter = getEntityManager().getReference(Fighter.class, v);
            return matchRoundFighter;
        }).collect(Collectors.toSet());
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MatchRound that = (MatchRound) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
