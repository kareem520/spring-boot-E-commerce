package com.ecomm.spring_ecomm.handler;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String message;
    private String code;
    List<ValidationErrors> validationErrorsList =  new ArrayList<>();


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ValidationErrors{
        public String field;
        public String message;
        public String code;
    }
}
