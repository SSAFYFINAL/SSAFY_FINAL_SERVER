
package com.ssafy.pjtaserver.utils;

import com.ssafy.pjtaserver.enums.ApiResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseUtil {
    private int code;
    private String message;
    private Object data;

    public static ResponseEntity<ApiResponseUtil> of(ApiResponseCode apiResponseCode, Object data) {
        ApiResponseUtil response = new ApiResponseUtil();
        response.setCode(apiResponseCode.getCode());
        response.setMessage(apiResponseCode.getMessage());
        response.setData(data);
        return new ResponseEntity<>(response, apiResponseCode.getHttpStatus());
    }

    public static ResponseEntity<ApiResponseUtil> of(ApiResponseCode apiResponseCode) {
        return of(apiResponseCode, null);
    }
}
