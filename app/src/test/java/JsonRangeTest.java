import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.hypersistence.utils.hibernate.type.range.Range;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lost.test.quarkus.common.RangeJsonSerde;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class JsonRangeTest {
    @Inject
    ObjectMapper objectMapper;

    @Test
    void rangeJsonSer() throws JsonProcessingException {
        var lower = "2023-10-21T03:20:45.971689Z";
        var upper = "2023-10-31T03:20:45.971689Z";
        var json = "{\"range\":\"[2023-10-21T03:20:45.971689Z,2023-10-31T03:20:45.971689Z]\"}";

        var v = new V(Range.closed(ZonedDateTime.parse(lower), ZonedDateTime.parse(upper)));
        assertThat(objectMapper.writeValueAsString(v)).isEqualTo(json);
    }

    @Test
    void rangeJsonDeser() throws JsonProcessingException {
        var lower = ZonedDateTime.parse("2023-10-21T03:20:45.971689Z");
        var upper = ZonedDateTime.parse("2023-10-31T03:20:45.971689Z");
        var str = """
            {
            "range": "[2023-10-21T03:20:45.971689Z,2023-10-31T03:20:45.971689Z)"
            }
            """;
        var v = objectMapper.readValue(str, V.class);
        assertThat(v).isNotNull();
        assertThat(v.range().lower()).isEqualTo(lower);
        assertThat(v.range().isLowerBoundClosed()).isTrue();
        assertThat(v.range().upper()).isEqualTo(upper);
        assertThat(v.range.isUpperBoundClosed()).isFalse();
    }

    public record V(
        @JsonSerialize(using = RangeJsonSerde.Ser.class)
        @JsonDeserialize(using = RangeJsonSerde.Deser.class)
        Range<ZonedDateTime> range
    ) {

    }
}
