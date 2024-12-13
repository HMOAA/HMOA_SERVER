package hmoa.hmoaserver.oauth.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.APPLE_DECODE_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        Object body = null;
        try (InputStream inputStream = response.body().asInputStream()) {
            String bod = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            log.info("Response body: {}", bod);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.body() != null) {
            try {
                body = objectMapper.readValue(response.body().toString(), Object.class);
            } catch (IOException e) {
                log.error("Error decoding response body", e);
            }
        }

        log.error("애플 소셜 로그인 Feign API Feign Client 호출 중 오류가 발생되었습니다. body: {}", body);

        return new CustomException(null, APPLE_DECODE_ERROR);
    }
}
