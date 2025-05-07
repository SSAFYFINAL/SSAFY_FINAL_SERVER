
package com.ssafy.pjtaserver.util.apiResponseUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    public static ResponseEntity<ApiResponse> of(ApiResponseCode apiResponseCode, Object data) {
        ApiResponse response = new ApiResponse();
        response.setCode(apiResponseCode.getCode());
        response.setMessage(apiResponseCode.getMessage());
        response.setData(data);
        return new ResponseEntity<>(response, apiResponseCode.getHttpStatus());
    }

    public static ResponseEntity<ApiResponse> of(ApiResponseCode apiResponseCode) {
        return of(apiResponseCode, null);
    }
}
