package project;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.redis.MessagePublisher;
import project.redis.RedisMessagePublisher;
import project.redis.RedisMessageSubscriber;
import project.service.MessageService;

import java.util.List;

@Configuration
@ComponentScan({"project.repository", "project.service", "project.redis"})
public class PipelineConfig {//implements WebMvcConfigurer {
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter(){
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return new MappingJackson2HttpMessageConverter(builder.build());
//    }
//
//    @Override
//// add default converters and override jacksonMessageConverter
//    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
//        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
//        stringConverter.setWriteAcceptCharset(false);
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(stringConverter);
//        messageConverters.add(new ResourceHttpMessageConverter());
//        messageConverters.add(new SourceHttpMessageConverter<>());
//        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
//        messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
//        messageConverters.add(jacksonMessageConverter());
//    }
    @Autowired
    private MessageService messageService;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber(messageService));
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }

    @Bean
    MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    }
}
