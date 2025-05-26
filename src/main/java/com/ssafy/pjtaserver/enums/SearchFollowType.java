package com.ssafy.pjtaserver.enums;

import lombok.Data;

public enum SearchFollowType {
    FOLLOWER("follower"),
    FOLLOWING("following");

    private String type;
    SearchFollowType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
