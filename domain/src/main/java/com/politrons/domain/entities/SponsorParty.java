package com.politrons.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SponsorParty {
    private String accountNumber;
    private String bankId;
    private String bankIdCode;
}
