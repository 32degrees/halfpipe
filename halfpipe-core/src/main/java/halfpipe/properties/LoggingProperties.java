package halfpipe.properties;

import ch.qos.logback.classic.Level;
import halfpipe.logging.LogbackEntry;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:12 AM
 */
@Component
//TODO: make ArchaiusProperties
@ConfigurationProperties("logging")
@Data
public class LoggingProperties {
    private List<LogbackEntry> loggers;
    private Level level;
}
