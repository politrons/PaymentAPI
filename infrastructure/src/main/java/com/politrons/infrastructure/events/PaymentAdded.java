package com.politrons.infrastructure.events;

import com.politrons.infrastructure.dto.PaymentInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAdded {

    private String id;
    private String type;
    private float version;
    PaymentInfoDTO paymentInfo;

}
