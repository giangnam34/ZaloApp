package com.essay.zaloapp.domain.payload.response.Authorize;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResultSendSMSResponse {
    private String status;
    private String code;
    private List<Data> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Data{
        private String phone;
        private Long status;
    }
}
