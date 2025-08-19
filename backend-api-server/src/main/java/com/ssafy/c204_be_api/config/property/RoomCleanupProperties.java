package com.ssafy.c204_be_api.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "room.cleanup")
public class RoomCleanupProperties {

    private final long intervalMs;
    private final long disconnectTimeoutSec;

    @ConstructorBinding
    public RoomCleanupProperties(long intervalMs, long disconnectTimeoutSec) {
        this.intervalMs = intervalMs;
        this.disconnectTimeoutSec = disconnectTimeoutSec;
    }
}
