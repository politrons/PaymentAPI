package com.politrons.application.model.payload.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatePayload {

    private String id;
    private String type;
    private float version;
    PaymentInfoPayload paymentInfo;

}
