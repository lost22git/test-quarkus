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

import static jakarta.persistence.FetchType.LAZY;

@Schema(title = "MatchFighter")
@RegisterForReflection
@Entity
@Table(name = "match_fighter", indexes = {
    @Index(name = "uk-match_id-fighter_id", unique = true, columnList = "match_id,fighter_id")
})
public class MatchFighter extends PanacheEntity {

    @JsonIgnore
    @Schema(title = "比赛")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id", nullable = false, updatable = false)
    public Match match;


    //    @JsonIgnore
    @Schema(title = "选手")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fighter_id", nullable = false, updatable = false)
    public Fighter fighter;

    @Schema(title = "时间范围", implementation = String.class, example = "[2020-02-02T00:00:00Z,2022-02-02T00:00:00Z]")
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange")
    public Range<ZonedDateTime> timeRange;


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MatchFighter that = (MatchFighter) object;
        return Objects.equals(match, that.match) && Objects.equals(fighter, that.fighter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, fighter);
    }
}
