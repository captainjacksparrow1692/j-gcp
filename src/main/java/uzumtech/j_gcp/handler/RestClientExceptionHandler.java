package uzumtech.j_gcp.handler; // Твой пакет

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import uzumtech.j_gcp.exception.HttpServerException;

import java.io.IOException;
import java.net.URI;

@Component
public class RestClientExceptionHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // Проверяем, является ли статус код ошибкой (4xx или 5xx)
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        // Формируем красивое сообщение об ошибке для логов
        var errorMessage = String.format("HTTP Error during %s request to %s. Status: %s",
                method, url, response.getStatusCode());

        if (response.getStatusCode().is4xxClientError()) {
            // Ошибки клиента (например, 404 Not Found или 400 Bad Request)
            throw new HttpClientException(errorMessage, response.getStatusCode());
        } else if (response.getStatusCode().is5xxServerError()) {
            // Ошибки сервера (например, 500 Internal Server Error)
            throw new HttpServerException(errorMessage, response.getStatusCode());
        }
    }
}