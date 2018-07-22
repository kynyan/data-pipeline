package project;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@ContextConfiguration(classes = {PersistenceConfig.class, WebSocketConfig.class, PipelineConfig.class})
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {
}
