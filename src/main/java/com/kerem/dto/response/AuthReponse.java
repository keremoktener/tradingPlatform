package com.kerem.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AuthReponse {

    private String jwt;
    private Boolean status;
    private String message;
    private Boolean isTwoFactorAuthEnabled;
    private String session;

}
