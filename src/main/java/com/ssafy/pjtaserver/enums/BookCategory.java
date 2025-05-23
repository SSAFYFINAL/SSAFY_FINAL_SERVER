package com.ssafy.pjtaserver.enums;

import lombok.Getter;

import java.util.Calendar;

@Getter
public enum BookCategory {
    SOCCER( "축구"),
    BASEBALL( "야구"),
    BASKETBALL( "농구"),
    TENNIS("테니스"),
    AQUATICSPORTS("수상레저스포츠"),
    RIDING("승마"),
    RACING("카레이싱,자전거"),
    NORMAL_SPORTS("일반 스포츠"),
    DANCE_SPORTS("에어로빅, 댄스"),
    RUNNING("마라톤");

    private final String categoryName;

    BookCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
