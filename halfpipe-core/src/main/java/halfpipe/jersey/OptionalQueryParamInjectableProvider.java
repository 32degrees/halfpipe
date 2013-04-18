package halfpipe.jersey;

import com.google.common.base.Optional;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.*;
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractorFactory;
import com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderFactory;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import org.springframework.stereotype.Service;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/jersey/OptionalQueryParamInjectableProvider.java
@Provider
@Service
public class OptionalQueryParamInjectableProvider implements InjectableProvider<QueryParam, Parameter> {
    @Context private ProviderServices services;
    private MultivaluedParameterExtractorFactory factory;

    public OptionalQueryParamInjectableProvider(){//@Context ProviderServices services) {
        //this.services = services;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic,
                                       QueryParam a,
                                       Parameter c) {
        if (isExtractable(c)) {
            final OptionalExtractor extractor = new OptionalExtractor(getFactory().get(unpack(c)));
            return new QueryParamInjectable(extractor, !c.isEncoded());
        }
        return null;
    }

    private boolean isExtractable(Parameter param) {
        return (param.getSourceName() != null) && !param.getSourceName().isEmpty() &&
                param.getParameterClass().isAssignableFrom(Optional.class) &&
                (param.getParameterType() instanceof ParameterizedType);
    }

    private Parameter unpack(Parameter param) {
        final Type typeParameter = ((ParameterizedType) param.getParameterType()).getActualTypeArguments()[0];
        return new Parameter(param.getAnnotations(),
                             param.getAnnotation(),
                             param.getSource(),
                             param.getSourceName(),
                             typeParameter,
                             (Class<?>) typeParameter,
                             param.isEncoded(),
                             param.getDefaultValue());
    }

    private MultivaluedParameterExtractorFactory getFactory() {
        if (factory == null) {
            final StringReaderFactory stringReaderFactory = new StringReaderFactory();
            stringReaderFactory.init(services);

            this.factory = new MultivaluedParameterExtractorFactory(stringReaderFactory);
        }

        return factory;
    }
}
