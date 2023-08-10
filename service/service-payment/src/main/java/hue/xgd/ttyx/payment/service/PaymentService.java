package hue.xgd.ttyx.payment.service;

import hue.xgd.ttyx.enums.PaymentType;
import hue.xgd.ttyx.model.order.PaymentInfo;

import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/10 15:23
 * @Description:
 */
public interface PaymentService {

    /**
     * 保存交易记录
     * @param orderNo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType);

    PaymentInfo getPaymentInfo(String orderNo, PaymentType paymentType);

    //支付成功
    void paySuccess(String orderNo,PaymentType paymentType, Map<String,String> paramMap);
}
