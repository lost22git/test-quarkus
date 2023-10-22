package lost.test.quarkus.common;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.hypersistence.utils.hibernate.type.range.Range;

import java.io.IOException;

public interface RangeJsonSerde {

    class Ser extends StdSerializer<Range<?>> {
        private static final ObjectMapper mapper;
        public static final SimpleModule module;


        static {
            mapper = new ObjectMapper();
            var typeFactory = mapper.getTypeFactory();

            module = new SimpleModule("lost")
                .addSerializer(new RangeJsonSerde.Ser(typeFactory.constructType(
                    new TypeReference<Range<?>>() {
                    })))
                .addDeserializer(Range.class, new RangeJsonSerde.Deser<>(typeFactory.constructType(
                    new TypeReference<Range<?>>() {
                    })));

            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.registerModule(module);
        }

        public Ser() {
            this(
                mapper.getTypeFactory().constructType(
                    new TypeReference<Range<?>>() {
                    })
            );
        }

        protected Ser(JavaType t) {
            super(t);
        }


        @Override
        public void serialize(Range<?> range,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            var lower = range.lower();
            var upper = range.upper();

            if (lower == null && upper == null && range.isBoundedOpen()) {
                jsonGenerator.writeString("()");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(range.hasMask(Range.LOWER_INCLUSIVE) ? '[' : '(')
                .append(range.hasLowerBound() ? trimQuote(mapper.writeValueAsString(lower)) : "")
                .append(",")
                .append(range.hasUpperBound() ? trimQuote(mapper.writeValueAsString(upper)) : "")
                .append(range.hasMask(Range.UPPER_INCLUSIVE) ? ']' : ')');

            jsonGenerator.writeString(sb.toString());
        }

        private String trimQuote(String str) {
            var len = str.length();
            return new StringBuilder(str).substring(1, len - 1);
        }

    }

    class Deser<T extends Comparable<? super T>> extends StdDeserializer<Range<T>> implements ContextualDeserializer {

        public Deser() {
            this(
                Ser.mapper.getTypeFactory().constructType(new TypeReference<Range<?>>() {
                })
            );
        }

        protected Deser(JavaType type) {
            super(type);
        }

        private JavaType type;

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                    BeanProperty property) throws JsonMappingException {

            this.type = property.getType().containedType(0);
            return this;
        }

        @Override
        public Range<T> deserialize(JsonParser p,
                                    DeserializationContext ctxt) throws IOException, JacksonException {
            var text = p.getText();
            var cls = (Class<T>) type.getRawClass();

            return Range.ofString(text, v -> {
                try {
                    return ctxt.readTreeAsValue(new TextNode(v), cls);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, cls);
        }
    }

}
