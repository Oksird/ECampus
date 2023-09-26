package ua.foxminded.muzychenko;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ua.foxminded.muzychenko")
@SpringBootConfiguration
@EnableAutoConfiguration
@PropertySource("classpath:data.properties")
public class DataConfiguration {

}
