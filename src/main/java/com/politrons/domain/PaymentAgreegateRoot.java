package com.politrons.domain;

import com.politrons.domain.entities.PaymentInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentAgreegateRoot {

    private String type;
    private String id;
    private float version;
    private String organisation_id;
    PaymentInfo paymentInfo;

}
