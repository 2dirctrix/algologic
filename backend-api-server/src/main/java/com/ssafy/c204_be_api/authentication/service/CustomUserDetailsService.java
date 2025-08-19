package com.ssafy.c204_be_api.authentication.service;


import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 주어진 사용자 ID(username)를 기반으로 DB에서 회원 정보를 조회하고,
     * Spring Security에서 사용할 수 있는 {@link UserDetails} 객체로 반환합니다.
     *
     * <p>
     * 인증 과정 중 Principal 객체 생성을 위해 호출되며, 사용자가 존재하지 않으면
     * {@link EntityNotFoundException}을 발생시켜 인증을 실패시킵니다.
     * </p>
     *
     * @param username JWT 클레임에서 추출한 사용자 ID (여기선 Long 형태의 ID)
     * @return {@link UserDetails} 구현체인 {@link CustomUserDetails} 객체
     * @throws EntityNotFoundException 사용자가 DB에 존재하지 않을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long id = Long.valueOf(username);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id = %s 에 해당하는 회원이 존재하지 않습니다.".formatted(id)));

        return CustomUserDetails.builder()
                .member(member)
                .build();
    }
}
