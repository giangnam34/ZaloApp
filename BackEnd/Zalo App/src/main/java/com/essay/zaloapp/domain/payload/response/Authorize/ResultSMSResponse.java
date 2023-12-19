package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResultSMSResponse {

    private String status;
    private String code;
    private Data data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Data{
        private Long tranId;
        private String name;
        private Long totalSMS;
        private Long totalPrice;
        private List<Object> invalidPhone;
    }
}
