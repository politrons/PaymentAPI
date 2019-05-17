package com.politrons.application.model.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SponsorPartyRequest {
    private String accountNumber;
    private String bankId;
    private String bankIdCode;
}
