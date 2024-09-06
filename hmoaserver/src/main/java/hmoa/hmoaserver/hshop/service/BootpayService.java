package hmoa.hmoaserver.hshop.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.dto.BootpayCancelRequstDto;
import hmoa.hmoaserver.hshop.dto.BootpayConfirmRequestDto;
import hmoa.hmoaserver.hshop.service.constant.BootpayConstant;
import hmoa.hmoaserver.member.domain.Member;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BootpayService {

    private final OrderService orderService;

    private Bootpay bootpay;

    @Value("${bootpay.key.private}")
    private String privateKey;
    @Value("${bootpay.key.rest-api}")
    private String restApiKey;

    // 토큰 가져오기
    // 에러 코드 존재할 시 부트페이측 에러로 반환
    private void getBootpayToken() {
        try {
            bootpay = new Bootpay(restApiKey, privateKey);
            HashMap token = bootpay.getAccessToken();
            if (token.get(BootpayConstant.ERROR_CODE) != null) {
                log.info("error code: " + token.get(BootpayConstant.ERROR_CODE));
                log.info("error content: " + token.get("message"));
                throw new CustomException(null, Code.BOOTPAY_ERROR);
            }
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    // 단 건 조회
    private HashMap findOneReceipt(String receiptId) {
        try {
            getBootpayToken();
            return bootpay.getReceipt(receiptId);
        } catch (Exception e) {
            throw new CustomException(null, Code.BOOTPAY_ERROR);
        }
    }

    // 결제 승인
    @Transactional
    public HashMap confirm(String receiptId, OrderEntity order) {
        try {
            getBootpayToken();
            HashMap res = bootpay.confirm(receiptId);

            if (res.get(BootpayConstant.ERROR_CODE) != null) {
                orderService.updateOrderStatus(order, OrderStatus.PAY_COMPLETE);
                return res;
            }

            log.info("결제 실패 : {}", res.get(BootpayConstant.ERROR_CODE));
            return res;
        } catch (Exception e) {
            new CustomException(null, Code.BOOTPAY_ERROR);
            return null;
        }
    }

    @Transactional
    public HashMap checkPayment(BootpayConfirmRequestDto dto) {

        // 결제 영수증 받아오기
        HashMap res = findOneReceipt(dto.getReceiptId());

        // 결제 금액과 주문 정보 금액이 동일한 지 확인
        int payPrice = Integer.parseInt(res.get(BootpayConstant.PRICE).toString());
        log.info("{}", res.get(BootpayConstant.ORDER_ID));
        OrderEntity order = orderService.findById(Long.valueOf(res.get(BootpayConstant.ORDER_ID).toString()));

        if (isSamePrice(payPrice, order.getTotalPrice())) {
            return confirm(dto.getReceiptId(), order);
        }

        order.updateOrderStatus(OrderStatus.PAY_FAILED);
        return null;
    }

    @Transactional
    public HashMap cancelPayment(BootpayCancelRequstDto dto, Member member) {
        try {
            getBootpayToken();
            Cancel cancel = new Cancel();
            cancel.receiptId = dto.getReceiptId();
            cancel.cancelUsername = member.getNickname();
            cancel.cancelMessage = dto.getCancelReason();

            HashMap res = bootpay.receiptCancel(cancel);

            if (res.get(BootpayConstant.ERROR_CODE) != null) {
                throw new CustomException(null, Code.BOOTPAY_ERROR);
            }

            return res;
        } catch (Exception e) {
            throw new CustomException(null, Code.BOOTPAY_ERROR);
        }
    }

    private boolean isSamePrice(int payPrice, int orderPrice) {
        return payPrice == orderPrice;
    }
}
