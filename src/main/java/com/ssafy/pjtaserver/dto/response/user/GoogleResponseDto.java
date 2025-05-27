package com.ssafy.pjtaserver.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleResponseDto {
    private String id;
    private String  email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;
    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;
    private String picture;
}
/*
구글 에서 넘겨주는 정보 자료구조 그중 내가 필요한것만 빼서쓰자
response : <200 OK OK,{
                        id=101221896386802153347, -o
                        email=do8351@gmail.com, -o
                        verified_email=true, -o
                        name=김도현, -o
                        given_name=도현, -o
                        family_name=김, -o
                        picture=https://lh3.googleusercontent.com/a/ACg8ocLgXwG3OzSkOEqhwJj6mlbswCLwL0ec2491i9qdrDC_Fvnj7A=s96-c}, -o
                        [Pragma:"no-cache",
                            Date:"Sun,
                            11 May 2025 12:50:40 GMT",
                            Expires:"Mon, 01 Jan 1990 00:00:00 GMT",
                            Cache-Control:"no-cache,
                            no-store,
                            max-age=0,
                            must-revalidate",
                            Content-Type:"application/json; charset=UTF-8", Vary:"X-Origin", "Referer", "Origin,Accept-Encoding", Server:"ESF", X-XSS-Protection:"0", X-Frame-Options:"SAMEORIGIN", X-Content-Type-Options:"nosniff", Alt-Svc:"h3=":443"; ma=2592000,h3-29=":443"; ma=2592000", Accept-Ranges:"none", Transfer-Encoding:"chunked"]>
 */