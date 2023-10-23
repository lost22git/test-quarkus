package lost.test.quarkus.model;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lost.test.quarkus.common.RangeLowerBound;
import lost.test.quarkus.common.RangeUpperBound;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.Set;

@Schema(title = "MatchRoundCreateParam")
public record MatchRoundCreateParam(
    @Schema(title = "比赛 id")
    long matchId,
    @Schema(title = "时间范围", implementation = String.class, example = "[2020-02-02T00:00:00Z,2022-02-02T00:00:00Z]")
    @NotNull
    @RangeLowerBound
    @RangeUpperBound
    Range<ZonedDateTime> timeRange,

    @Schema(title = "选手 id 列表")
    @NotEmpty
    Set<@NotNull Long> fighterIds
) {

}
