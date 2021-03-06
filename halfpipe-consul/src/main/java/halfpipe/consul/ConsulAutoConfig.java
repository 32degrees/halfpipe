package halfpipe.consul;

import com.google.common.base.Supplier;
import halfpipe.client.ClientConfigurer;
import halfpipe.core.HalfpipeAutoConfig;
import halfpipe.consul.client.AgentClient;
import halfpipe.consul.client.CatalogClient;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.KeyValue;
import halfpipe.properties.PropertiesSourceFactory;
import halfpipe.util.BeanUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 9:16 PM
 */
@Configuration
@AutoConfigureAfter(HalfpipeAutoConfig.class)
public class ConsulAutoConfig extends ClientConfigurer {

    @Inject
    BeanUtils beanUtils;

    @Bean
    ConsulProperties consulProperties() {
        return new ConsulProperties();
    }

    @Bean
    ConsulEndpointProps consulEndpointProps() {
        return new ConsulEndpointProps();
    }

    @Bean
    PropertiesSourceFactory consulPropertiesSourceFactory() {
        return new ConsulPropertiesSourceFactory();
    }

    @Bean
    ConsulPropertiesSource consulPropertiesSource() {
        return new ConsulPropertiesSource();
    }

    @Bean
    ConsulEndpoint consulEndpoint() {
        return new ConsulEndpoint(consulEndpointProps());
    }

    @Bean
    ShutdownDeregister shutdownDeregister() {
        return new ShutdownDeregister();
    }

    @Bean(name = "KVClient.getKeyValueRecurse.fallback")
    Supplier<List<KeyValue>> getKeyValueRecurseFallback() {
        return new Supplier<List<KeyValue>>() {
            @Override
            public List<KeyValue> get() {
                return new ArrayList<>();
            }
        };
    }

    @Bean(name = "AgentClient.register.fallback")
    Supplier<Void> agentClientRegisterFallback() {
        return new Supplier<Void>() {
            @Override
            public Void get() {
                //TODO LOG Warning or error, retry later
                return null;
            }
        };
    }

    @Bean
    KVClient kvClient() {
        return client().target(KVClient.class, consulProperties().getUrl());
    }

    @Bean
    AgentClient agentClient() {
        return client().target(AgentClient.class, consulProperties().getUrl());
    }

    @Bean
    CatalogClient catalogClient() {
        return client().target(CatalogClient.class, consulProperties().getUrl());
    }
}
