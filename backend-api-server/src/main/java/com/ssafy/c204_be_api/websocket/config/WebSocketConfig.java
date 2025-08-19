package com.ssafy.c204_be_api.websocket.config;

import com.ssafy.c204_be_api.websocket.interceptor.JwtChannelInterceptor;
import com.ssafy.c204_be_api.websocket.interceptor.JwtHandshakeInterceptor;
import com.ssafy.c204_be_api.websocket.interceptor.WebSocketConnectionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final JwtChannelInterceptor jwtChannelInterceptor;
    private final WebSocketConnectionInterceptor webSocketConnectionInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/ws");
        registry.setUserDestinationPrefix("/user");  // 기본값인데 명시하겠음
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // 개발 중일때만 임시 허용
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS()                   // fallback 지원
                .setSessionCookieNeeded(true);  // SockJS fallback 모드 시 필요
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 1. 인증
        registration.interceptors(jwtChannelInterceptor);
        // 2. 연결 상태/중복 여부 검사
        registration.interceptors(webSocketConnectionInterceptor);
    }
}
