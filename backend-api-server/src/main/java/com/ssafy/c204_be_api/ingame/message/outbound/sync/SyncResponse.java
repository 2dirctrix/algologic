package com.ssafy.c204_be_api.ingame.message.outbound.sync;

import java.util.List;

public interface SyncResponse<P extends PlayerSync> {
    List<P> getPlayers();
    P getMe();
}