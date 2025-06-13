package io.charlie.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Charlie Zhang
 * @version v1.0
 * @date 19/04/2025
 * @description 前端用String类型的雪花ID保持精度，后端及数据库继续使用Long(BigINT)类型不影响数据库查询执行效率
 */
@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 全局配置序列化返回 JSON 处理
        SimpleModule simpleModule = new SimpleModule();

        // JSON Long ==> String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);

        // JSON Integer ==> String
        simpleModule.addSerializer(Integer.class, ToStringSerializer.instance);

        // LocalDateTime ==> EpochMilli
        simpleModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, IOException {
                gen.writeNumber(value.toInstant(ZoneOffset.UTC).toEpochMilli());
            }
        });

        // Date ==> EpochMilli
        simpleModule.addSerializer(java.util.Date.class, new JsonSerializer<java.util.Date>() {
            @Override
            public void serialize(java.util.Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, IOException {
                gen.writeNumber(value.getTime());
            }
        });

        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }
}
