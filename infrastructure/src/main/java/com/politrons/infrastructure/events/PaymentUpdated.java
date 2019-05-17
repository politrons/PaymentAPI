package com.politrons.infrastructure.events;

import com.politrons.infrastructure.dto.PaymentInfoDTO;

public class PaymentUpdated {

    private String id;
    private String type;
    private float version;
    PaymentInfoDTO paymentInfo;

}
