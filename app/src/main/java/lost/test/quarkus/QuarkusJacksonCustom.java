package lost.test.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;
import lost.test.quarkus.common.RangeJsonSerde;

@Singleton
public class QuarkusJacksonCustom implements ObjectMapperCustomizer {
    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper.registerModule(RangeJsonSerde.Ser.module);
    }
}
