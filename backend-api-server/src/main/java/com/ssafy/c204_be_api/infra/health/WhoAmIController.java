package com.ssafy.c204_be_api.infra.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Profile("local")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health 체크 API")
class WhoAmIController {
    private final String instanceId = System.getenv().getOrDefault("HOSTNAME", UUID.randomUUID().toString());

    /**
     * 다중 인스턴스 확인용
     * @return 응답 서버의 인스턴스 ID
     */
    @Operation(
            summary = "응답 서버 인스턴스 확인 API",
            description = "응답 서버의 인스턴스 ID를 반환합니다."
    )
    @GetMapping("/whoami")
    ResponseEntity<String> who() {
        return ResponseEntity.ok()
                .header("X-Instance-Id", instanceId)
                .body(instanceId);
    }
}