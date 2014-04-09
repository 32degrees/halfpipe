package halfpipe.properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.TypeUtils;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.springframework.core.annotation.AnnotationUtils.*;
import static org.springframework.util.ReflectionUtils.*;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 2:00 AM
 */
//TODO: extend ConfigurationPropertiesBindingPostProcessor but remove DynaProps?
public class ArchaiusPropertiesProcessor implements BeanPostProcessor {

    @Inject
    ConversionService conversionService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ArchaiusProperties annotation = findAnnotation(bean.getClass(), ArchaiusProperties.class);
        if (annotation != null)
            postProcessAfterInitialization(bean, beanName, annotation);
        return bean;
    }

    protected void postProcessAfterInitialization(Object bean, String beanName, ArchaiusProperties annotation) {
        doWithFields(bean.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (TypeUtils.isAssignable(field.getType(), DynaProp.class)) {
                    // make the field accessible if defined private
                    ReflectionUtils.makeAccessible(field);
                    Type genericType = field.getGenericType();
                    Type typeArgument = null;
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) genericType;
                        Type[] typeArguments = pType.getActualTypeArguments();
                        if (typeArguments != null && typeArguments.length == 1) {
                            typeArgument = typeArguments[0];
                        }
                    }

                    DynaProp defaultProp = (DynaProp) field.get(bean);
                    Object defaultValue = null;
                    if (defaultProp != null) {
                        defaultValue = defaultProp.getValue();
                        if (typeArgument == null) {
                            typeArgument = defaultValue.getClass();
                        }
                    }

                    String propertyName = field.getName();
                    if (isNotBlank(annotation.value())) {
                        propertyName = annotation.value() + "." + propertyName;
                    }
                    DynamicProp dynamicProp = new DynamicProp(propertyName, defaultValue, (Class<?>) typeArgument);
                    dynamicProp.conversionService = conversionService;
                    field.set(bean, dynamicProp);
                } else {
                    throw new IllegalArgumentException(
                            "Field type of field annoteted with Log annotation. Expected field type: "
                                    + DynaProp.class.getCanonicalName());
                }
            }
        });
    }
}