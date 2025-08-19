package com.ssafy.c204_be_api.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProgrammingLanguage {
    JAVA("Java"),
    CPP("C++"),
    PYTHON("Python");

    private final String displayName;

    public static boolean existsByDisplayName(String name) {
        return Arrays.stream(values())
                .anyMatch(lang -> lang.getDisplayName().equalsIgnoreCase(name));
    }

    public static ProgrammingLanguage fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(lang -> lang.getDisplayName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid programming language: " + name));
    }
}
