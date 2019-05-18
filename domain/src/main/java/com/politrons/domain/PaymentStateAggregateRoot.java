package com.politrons.domain;

import com.politrons.domain.entities.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStateAggregateRoot {

    private String id;
    private String type;
    private float version;
    PaymentInfo paymentInfo;

    /**
     * Factory to create a PaymentStateAggregateRoot with state created.
     * Since this is the first event for a payment we set the rowId also as paymentId
     */
    public static PaymentStateAggregateRoot create(PaymentInfo paymentInfo) {
        String id = UUID.randomUUID().toString();
        paymentInfo.setPaymentId(id);
        return new PaymentStateAggregateRoot(id, "created", 0, paymentInfo);
    }

    /**
     * Factory to create a PaymentStateAggregateRoot with state changed
     */
    public static PaymentStateAggregateRoot update(PaymentInfo paymentInfo) {
        String id = UUID.randomUUID().toString();
        return new PaymentStateAggregateRoot(id, "changed", 0, paymentInfo);
    }

    /**
     * Factory to update a previous PaymentStateAggregateRoot with state deleted
     * In case of delete since we search previously the domain, we just need to update the rowId and set the state.
     */
    public static PaymentStateAggregateRoot delete(PaymentStateAggregateRoot paymentStateAggregateRoot) {
        String id = UUID.randomUUID().toString();
        paymentStateAggregateRoot.setId(id);
        paymentStateAggregateRoot.setType("deleted");
        return paymentStateAggregateRoot;
    }


}
