package project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperFactory {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Object mapper created using constructor differs from one used by spring to write/read http requests:
     * - 'spring mapper' by default will convert LocalDateTime (thinking thta it is in system timezone) to UTC
     * - 'spring mapper' will output date as ISO string
     *
     *
     * Serializes/deserializes LocalDateTime fields into/from ISO 8601 format, for example: "2017-09-07T14:57:22.157".
     * Default object mapper configuration serializes LocalDateTime fields as JSON objects {"hour": 14, "minute": 37, "second": 58, ... },
     * which subsequently can't be deserialized by the object mapper. It says in that case: "No suitable constructor found for type [simple type, class java.time.LocalDateTime]..."
     */
    public static final ObjectMapper OBJECT_MAPPER_WITH_TIMESTAMPS = initObjectMapperWithTimestamps();

    private static ObjectMapper initObjectMapperWithTimestamps() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

}
