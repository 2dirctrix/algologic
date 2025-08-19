package com.ssafy.c204_be_api.member.repository.projection;

import java.sql.Date;

public interface DailyCountProjection {
    Date getDate();
    Long getCnt();
}
