package lost.test.quarkus.model;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lost.test.quarkus.common.RangeLowerBound;
import lost.test.quarkus.common.RangeUpperBound;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.Set;

@Schema(description = "MatchRoundCreateParam")
public record MatchRoundCreateParam(
    @Schema(description = "比赛 id")
    long matchId,
    @Schema(description = "时间范围")
    @NotNull
    @RangeLowerBound
    @RangeUpperBound
    Range<ZonedDateTime> timeRange,

    @Schema(description = "选手 id 列表")
    @NotEmpty
    Set<@NotNull Long> fighterIds
) {

}
