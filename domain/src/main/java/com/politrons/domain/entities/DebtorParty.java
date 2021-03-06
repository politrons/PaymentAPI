package com.politrons.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtorParty {
    private String accountName;
    private String accountNumber;
    private float accountType;
    private String address;
    private String bankId;
    private String name;

    public static DebtorParty create(String accountName,
                              String accountNumber,
                              float accountType,
                              String address,
                              String bankId,
                              String name) {
        return new DebtorParty(accountName, accountNumber, accountType, address, bankId, name);

    }
}
