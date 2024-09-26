package hmoa.hmoaserver.admin;

import hmoa.hmoaserver.admin.dto.*;
import hmoa.hmoaserver.common.DateUtils;
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

    public AdminFacade(WebClient.Builder webClientBuilder, OrderService orderService) {
        this.webClient = webClientBuilder.baseUrl("https://apis.tracker.delivery").build();
        this.orderService = orderService;
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
        log.info("check Tracking");
    }

    public void updateOrderStatus(OrderStatusUpdateRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, dto.getStatus());
    }

    public Mono<String> registerTrackWebhook(OrderDeliverySaveRequestDto dto) {
        String expirationTime = DateUtils.extractUTC(LocalDateTime.now().plusDays(5));
        WebhookInput webhookInput = new WebhookInput(dto.getTrackingNumber(), trackingCallbackUrl, expirationTime);

        RegisterTrackingRequestDto request = new RegisterTrackingRequestDto(Map.of("input", webhookInput));

        return webClient.post()
                .uri("/graphql")
                .header(HttpHeaders.AUTHORIZATION, "TRACKQL-API-KEY " + trackingAccess + ":" + trackingSecret)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)  // DTO를 body로 설정
                .retrieve()
                .bodyToMono(String.class);
    }
}
