package hmoa.hmoaserver.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import hmoa.hmoaserver.admin.dto.*;
import hmoa.hmoaserver.admin.dto.constant.TrackingQuery;
import hmoa.hmoaserver.admin.dto.constant.TrackingStatus;
import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class AdminFacade {

    @Value("${tracking.access}")
    private String trackingAccess;
    @Value("${tracking.secret}")
    private String trackingSecret;
    @Value("${tracking.callback-url}")
    private String trackingCallbackUrl;

    private final WebClient webClient;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public AdminFacade(WebClient.Builder webClientBuilder, OrderService orderService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://apis.tracker.delivery").build();
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    // 운송장 등록
    public void saveDeliveryInfo(OrderDeliverySaveRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, OrderStatus.SHIPPING_PROGRESS);
        orderService.updateDeliveryInfo(order, dto.getCourierCountry(), dto.getTrackingNumber());
    }

    // 배송 상태 변화 받을 시 처리하는 로직
    public void checkTracking(TrackingCallbackRequestDto dto) {
        OrderEntity order = orderService.getByTrackingNumber(dto.getTrackingNumber());
        TrackingDeliveryRequestDto request = new TrackingDeliveryRequestDto(TrackingQuery.CHECK_QUERY.getQuery(), dto);
        TrackingResponseDto response = webPost(request).map(this::mapToTrackingResponseDto).block();
        String status = null;

        if (response != null) {
            status = response.getData().getTrack().getLastEvent().getStatus().getCode();
        }

        if (status != null && (status.equals(TrackingStatus.AVAILABLE_FOR_PICKUP.getValue()) || status.equals(TrackingStatus.DELIVERED.getValue()))) {
            orderService.updateOrderStatus(order, OrderStatus.SHIPPING_COMPLETE);
        }
    }

    public void updateOrderStatus(OrderStatusUpdateRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, dto.getStatus());
    }

    public Mono<String> registerTrackWebhook(OrderDeliverySaveRequestDto dto) {
        String expirationTime = DateUtils.extractUTC(LocalDateTime.now().plusDays(5));
        WebhookInput webhookInput = new WebhookInput(dto.getTrackingNumber(), trackingCallbackUrl, expirationTime);
        TrackingDeliveryRequestDto request = new TrackingDeliveryRequestDto(TrackingQuery.REGISTER_QUERY.getQuery(), Map.of("input", webhookInput));
        return webPost(request);
    }

    /**
     * 20x 이외의 코드 반환시 무한 요청한다고 한다..
     * 오류가 나도 일단 200번 보내도록 에러 처리
     */
    private Mono<String> webPost(Object request) {
        try {
            return webClient.post()
                    .uri("/graphql")
                    .header(HttpHeaders.AUTHORIZATION, "TRACKQL-API-KEY " + trackingAccess + ":" + trackingSecret)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(request)  // DTO를 body로 설정
                    .retrieve()
                    .bodyToMono(String.class);
        } catch (Exception e) {
            throw new CustomException(e, Code.TRAKING_FAILED);
        }
    }

    private TrackingResponseDto mapToTrackingResponseDto(String response) {
        try {
            return objectMapper.readValue(response, TrackingResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(e, Code.TRAKING_FAILED);
        }
    }
}
