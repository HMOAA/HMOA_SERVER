package hmoa.hmoaserver.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import hmoa.hmoaserver.admin.dto.*;
import hmoa.hmoaserver.admin.dto.constant.TrackingQuery;
import hmoa.hmoaserver.admin.dto.constant.TrackingStatus;
import hmoa.hmoaserver.admin.service.TestTokenProvider;
import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.dto.MemberAddressResponseDto;
import hmoa.hmoaserver.member.dto.MemberInfoResponseDto;
import hmoa.hmoaserver.member.service.MemberAddressService;
import hmoa.hmoaserver.member.service.MemberInfoService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AdminFacade {

    private final MemberAddressService memberAddressService;
    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
    private final TestTokenProvider testTokenProvider;
    private final CommunityService communityService;

    @Value("${tracking.access}")
    private String trackingAccess;
    @Value("${tracking.secret}")
    private String trackingSecret;
    @Value("${tracking.callback-url}")
    private String trackingCallbackUrl;

    private final WebClient webClient;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public AdminFacade(WebClient.Builder webClientBuilder, OrderService orderService, ObjectMapper objectMapper, MemberAddressService memberAddressService, MemberInfoService memberInfoService, MemberService memberService, TestTokenProvider testTokenProvider, CommunityService communityService) {
        this.webClient = webClientBuilder.baseUrl("https://apis.tracker.delivery").build();
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.memberAddressService = memberAddressService;
        this.memberInfoService = memberInfoService;
        this.memberService = memberService;
        this.testTokenProvider = testTokenProvider;
        this.communityService = communityService;
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

    // 배송 보내야 할 주문 받아오기
    public List<OrderDeliveryListResponseDto> deliveryOrderList() {

        List<OrderEntity> orders = orderService.getDeliveryOrders();
        return orders.stream().map(order -> {
            log.info("{}", order.getId());
            MemberAddressResponseDto address = new MemberAddressResponseDto(memberAddressService.findByMemberId(order.getMemberId()));
            MemberInfoResponseDto info = new MemberInfoResponseDto(memberInfoService.findByMemberId(order.getMemberId()));
            return new OrderDeliveryListResponseDto(order, address, info);
        }).toList();
    }
  
    public String getMemberToken(Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new CustomException(null, Code.MEMBER_NOT_FOUND));
        return testTokenProvider.getMemberToken(member);
    }

    public void updateOrderStatus(OrderStatusUpdateRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, dto.getStatus());
    }

    public Mono<String> registerTrackWebhook(OrderDeliverySaveRequestDto dto) {
        String expirationTime = DateUtils.extractUTC(LocalDateTime.now().plusDays(5));
        WebhookInput webhookInput = new WebhookInput(dto.getTrackingNumber(), trackingCallbackUrl, expirationTime);
        TrackingDeliveryRequestDto request = new TrackingDeliveryRequestDto(TrackingQuery.REGISTER_QUERY.getQuery(), Map.of("input", webhookInput));
        return webPost(request)
                .map(response -> "성공")
                .onErrorReturn("실패");
    }

    public void deleteCommunity(Long communityId) {
        Community community = communityService.getCommunityById(communityId);
        Member member = community.getMember();
        communityService.deleteCommunity(member, communityId);
    }

    /**
     * 20x 이외의 코드 반환시 무한 요청한다고 한다..
     * 오류가 나도 일단 200번 보내도록 에러 처리
     */
    private Mono<String> webPost(Object request) {
        return webClient.post()
                .uri("/graphql")
                .header(HttpHeaders.AUTHORIZATION, "TRACKQL-API-KEY " + trackingAccess + ":" + trackingSecret)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)  // DTO를 body로 설정
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> {
                    throw new CustomException(null, Code.TRAKING_FAILED);
                });
    }

    private TrackingResponseDto mapToTrackingResponseDto(String response) {
        try {
            return objectMapper.readValue(response, TrackingResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(e, Code.TRAKING_FAILED);
        }
    }
}
