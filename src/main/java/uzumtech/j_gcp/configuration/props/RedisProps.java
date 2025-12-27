package uzumtech.j_gcp.configuration.props;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProps {

    String host;
    int port;

    int database;
    String username;
    String password;

    int defaultTtl;

    int timeout;
    int shutdownTimeout;

    int gcpTtl;
    int userTtl;
}
//взято из delivery