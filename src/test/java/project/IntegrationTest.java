package project;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.*;

@TestPropertySource("/test.properties")
@ContextConfiguration(classes = {PersistenceConfig.class, WebSocketConfig.class, PipelineConfig.class})
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {
}
