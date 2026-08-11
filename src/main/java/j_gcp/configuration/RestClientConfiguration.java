package j_gcp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import j_gcp.handler.RestClientExceptionHandler;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor // Позволяет внедрить RestClientExceptionHandler
public class RestClientConfiguration {

    private final RestClientExceptionHandler restClientExceptionHandler;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestFactory(clientHttpRequestFactory())
                // Используем внедренный хендлер вместо создания нового через new
                .defaultStatusHandler(restClientExceptionHandler)
                .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        var settings = HttpClientSettings
                .defaults()
                .withReadTimeout(Duration.ofMillis(5000))
                .withConnectTimeout(Duration.ofMillis(5000));

        // Используем JDK HttpClient
        return ClientHttpRequestFactoryBuilder.jdk().build(settings);
    }
}