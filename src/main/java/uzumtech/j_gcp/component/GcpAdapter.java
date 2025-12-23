package uzumtech.j_gcp.component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GcpAdapter {

    RestClient restClient;

    public void getUserInfo() {
        var result = restClient
                .get()
                .uri(
                        "https://api.ipify.org")
                .retrieve()
                .body(String.class);

        log.info("{}", result);
    }

}