package com.politrons.domain;

import com.politrons.domain.entities.PaymentInfo;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static Future<Either<Throwable, String>> create(PaymentInfo paymentInfo) {
        return Future.of(() -> Right("1981"));
    }

    public static void update(String id, PaymentInfo paymentInfo) {


    }


}
