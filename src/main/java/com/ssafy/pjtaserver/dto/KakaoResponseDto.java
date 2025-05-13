package com.ssafy.pjtaserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoResponseDto {
    private Long id;

    @JsonProperty("connected_at") // JSON 키 매핑
    private String connectedAt;

    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Data
    public static class Properties {
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Data
    public static class KakaoAccount {
        @JsonProperty("has_email")
        private Boolean hasEmail;

        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;

        private String email;

        private Profile profile;

        @Data
        public static class Profile {
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;
        }
    }
}
    /*
    카카오 에서 넘겨주는 자료 구조
    {
      "id": 4255163426,
      "connected_at": "2025-05-10T13:42:23Z",
      "properties": {
        "nickname": "김도현",
        "profile_image": "http://...profile.jpeg",
        "thumbnail_image": "http://...thumbnail.jpeg"
      },
      "kakao_account": {
        "profile_nickname_needs_agreement": false,
        "profile_image_needs_agreement": false,
        "profile": {
          "nickname": "김도현",
          "thumbnail_image_url": "http://...thumbnail.jpeg",
          "profile_image_url": "http://...profile.jpeg",
          "is_default_image": true,
          "is_default_nickname": false
        },
        "has_email": true,
        "email_needs_agreement": false,
        "is_email_valid": true,
        "is_email_verified": true,
        "email": "do9698@naver.com"
      }
    }
 */