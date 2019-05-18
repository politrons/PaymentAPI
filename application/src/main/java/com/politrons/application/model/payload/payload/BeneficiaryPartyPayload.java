package com.politrons.application.model.payload.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryPartyPayload {
    private String accountName;
    private String accountNumber;
    private float accountType;
    private String address;
    private String bankId;
    private String name;

}

