package com.politrons.domain;

import com.politrons.domain.entities.PaymentInfo;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static io.vavr.API.Right;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAggregateRoot {

    private String id;
    private String type;
    private float version;
    PaymentInfo paymentInfo;

    public static PaymentAggregateRoot create(PaymentInfo paymentInfo) {
        return new PaymentAggregateRoot(UUID.randomUUID().toString(), "payment", 0, paymentInfo);
    }

    public static void update(String id, PaymentInfo paymentInfo) {


    }


}
