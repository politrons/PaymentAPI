package com.politrons.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryPartyDTO {
    private String accountName;
    private String accountNumber;
    private float accountType;
    private String address;
    private String bankId;
    private String name;

}



