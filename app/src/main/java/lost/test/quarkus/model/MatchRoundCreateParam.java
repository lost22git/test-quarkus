package lost.test.quarkus.model;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.validation.constraints.NotNull;
import lost.test.quarkus.common.RangeLowerBound;
import lost.test.quarkus.common.RangeUpperBound;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "MatchRoundCreateParam")
public record MatchRoundCreateParam(
    @Schema(description = "match id")
    long matchId,


    @Schema(description = "时间范围")
    @NotNull
    @RangeLowerBound
    @RangeUpperBound
    Range<ZonedDateTime> timeRange

) {

}
