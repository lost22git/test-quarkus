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

import static jakarta.persistence.FetchType.LAZY;

@Schema(description = "格斗比赛回合")
@RegisterForReflection
@Entity
@Table(name = "match_round")
public class MatchRound extends PanacheEntity {
    @JsonIgnore
    @Schema(description = "格斗比赛")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id", nullable = false, updatable = false)
    public Match match;

    @Schema(description = "时间范围")
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    public Range<ZonedDateTime> timeRange;
}
