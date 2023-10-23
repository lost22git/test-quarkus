package lost.test.quarkus.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.util.Objects;

@RegisterForReflection
public class MatchRoundFighterId implements Serializable {
    public MatchRound matchRound;
    public Fighter fighter;

    public MatchRoundFighterId() {

    }

    public MatchRoundFighterId(MatchRound matchRound,
                               Fighter fighter) {
        this.matchRound = matchRound;
        this.fighter = fighter;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var pk = (MatchRoundFighterId) object;
        return Objects.equals(matchRound, pk.matchRound) && Objects.equals(fighter, pk.fighter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchRound, fighter);
    }
}
