package com.bookexchange.management.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDTO {
    private long id ;
    private String token;
    private String error;
}
