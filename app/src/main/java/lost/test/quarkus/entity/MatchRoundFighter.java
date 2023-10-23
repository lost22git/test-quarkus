package lost.test.quarkus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;

@Schema(title = "MatchRoundFighter")
@RegisterForReflection
@Entity
@Table(name = "match_round_fighter")
@IdClass(MatchRoundFighterId.class)
public class MatchRoundFighter extends PanacheEntityBase {

    @Id
    @JsonIgnore
    @Schema(title = "回合")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_round_id", nullable = false, updatable = false)
    public MatchRound matchRound;

    @Id
    @JsonIgnore
    @Schema(title = "选手")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fighter_id", nullable = false, updatable = false)
    public Fighter fighter;

    public long getFighterId() {
        return fighter.id;
    }

    public long matchRound() {
        return matchRound.id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MatchRoundFighter that = (MatchRoundFighter) object;
        return Objects.equals(matchRound, that.matchRound) && Objects.equals(fighter, that.fighter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchRound, fighter);
    }
}
