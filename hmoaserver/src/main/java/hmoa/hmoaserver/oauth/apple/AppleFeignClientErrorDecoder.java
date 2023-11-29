package hmoa.hmoaserver.oauth.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static hmoa.hmoaserver.exception.Code.APPLE_DECODE_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        Object body = null;
        if (response != null && response.body() != null) {
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
