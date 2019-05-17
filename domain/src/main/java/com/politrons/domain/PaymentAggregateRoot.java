package com.politrons.domain;

import com.politrons.domain.entities.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAggregateRoot {

    private String id;
    private String type;
    private float version;
    PaymentInfo paymentInfo;

    public static void create(PaymentInfo paymentInfo) {


    }

    public static void update(String id, PaymentInfo paymentInfo) {


    }


}
