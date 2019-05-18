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

    public static PaymentStateAggregateRoot create(PaymentInfo paymentInfo) {
        String id = UUID.randomUUID().toString();
        return new PaymentStateAggregateRoot(id, "created", 0, paymentInfo);
    }

    public static void update(String id, PaymentInfo paymentInfo) {


    }


}
