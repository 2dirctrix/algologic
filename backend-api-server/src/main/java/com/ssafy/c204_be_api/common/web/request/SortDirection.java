package com.ssafy.c204_be_api.common.web.request;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum SortDirection {

    ASC("asc"),
    DESC("desc");

    private final String description;

    public static boolean existsByDirection(String direction) {
        return Arrays.stream(values())
            .anyMatch(sortDirection -> sortDirection.getDescription().equalsIgnoreCase(direction));
    }
}
