package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.DynamicPropertyFactory
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[AppConfig]),
  excludeFilters = Array(new Filter (Array (classOf[Controller]))))
@ImportResource(Array ("classpath:META-INF/spring/applicationContext-security.xml"))
class AppConfig {

  @Bean @Named("helloText")
  def helloText() = DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default")

  @Bean
  @Scope("singleton")
  def jacksonScalaCollections() = new ScalaCollectionsQueryParamInjectableProvider()
}