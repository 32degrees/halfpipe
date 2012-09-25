package thirtytwo.degrees.halfpipe.config;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.Lists;
import com.yammer.metrics.HealthChecks;
import com.yammer.metrics.core.HealthCheck;
import com.yammer.metrics.core.HealthCheckRegistry;
import com.yammer.metrics.util.DeadlockHealthCheck;
import org.codehaus.jackson.map.*;
import org.springframework.context.annotation.*;
import thirtytwo.degrees.halfpipe.jersey.*;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@Configuration
public class DefaultAppConfg {

    @Bean @Scope("singleton")
    public OptionalQueryParamInjectableProvider optionalQueryParamInjectableProvider() {
        return new OptionalQueryParamInjectableProvider();
    }

    @Bean @Scope("singleton")
    public DeadlockHealthCheck deadlockHealthCheck() {
        return new DeadlockHealthCheck();
    }

    @Bean @Scope("singleton")
    public HealthCheckRegistry healthChecks(List<HealthCheck> healthChecks) {
        for (HealthCheck healthCheck : healthChecks) {
            HealthChecks.register(healthCheck);
        }
        return HealthChecks.defaultRegistry();
    }

    @Bean @Scope("singleton")
    public GuavaExtrasModule guavaExtrasModule() {
        return new GuavaExtrasModule();
    }

    @Bean @Scope("singleton")
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    @Bean @Scope("singleton")
    public HalfpipeObjectMapperProvider objectMapperProvider(AnnotationSensitivePropertyNamingStrategy namingStrategy,
                                                             List<Module> modules) {
        return new HalfpipeObjectMapperProvider(namingStrategy, modules);
    }

    @Bean @Scope("singleton")
    public AnnotationSensitivePropertyNamingStrategy jsonNamingStrategy() {
        return new AnnotationSensitivePropertyNamingStrategy();
    }
}
