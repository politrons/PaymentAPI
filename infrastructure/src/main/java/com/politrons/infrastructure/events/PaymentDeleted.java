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
public class PaymentDeleted implements PaymentEvent{
    String id;
    String type;
    float version;
    PaymentInfoDTO paymentInfo;
}
