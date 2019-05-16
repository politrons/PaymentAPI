package com.politrons.application;

import com.politrons.application.it.PaymentResourceTest;
import io.quarkus.test.junit.SubstrateTest;

@SubstrateTest
public class NativePaymentResourceIT extends PaymentResourceTest {

    // Execute the same tests but in native mode.
}