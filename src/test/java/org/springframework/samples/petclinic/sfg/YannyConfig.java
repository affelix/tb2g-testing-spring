package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Annotating a class with the @Configuration indicates that the class can be used
 * by the Spring IoC container as a source of bean definitions
 */
@Profile("base-test")
@Configuration
public class YannyConfig {

    /**
     * The @Bean annotation tells Spring that a method annotated with @Bean will return
     * an object that should be registered as a bean in the Spring application context.
     * @return YannyWordProducer
     */
    @Bean
    YannyWordProducer yarnWordProducer() {
        return new YannyWordProducer();
    }
}
