package com.ssafy.c204_be_api.authentication.dto;

import com.ssafy.c204_be_api.member.domain.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final String NOT_USE_PASSWORD_VALUE = "0";

    @NonNull
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        if(hasRole()) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return NOT_USE_PASSWORD_VALUE;
    }

    @Override
    public String getUsername() {
        return member.getId().toString();
    }

    private boolean hasRole() {
        return Objects.nonNull(member.getRole());
    }
}
