package com.ssafy.c204_be_api.member.web.event;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MemberEvent {
    RANKER_UPDATED,
}
