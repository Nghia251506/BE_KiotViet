package com.example.be_kiotviet.security;

import com.example.be_kiotviet.entity.User;
import com.example.be_kiotviet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Giờ thì role + permissions đã được load sẵn → không Lazy nữa!
        var authorities = user.getRole().getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getCode()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    // QUAN TRỌNG: Phải thêm prefix "ROLE_" trước roleCode
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String roleCode = user.getRole().getName(); // Ví dụ: "ADMIN", "USER"

        // Thêm prefix "ROLE_" để Spring Security hasRole() hoạt động
        String authorityName = "ROLE_" + roleCode;

        return Collections.singletonList(new SimpleGrantedAuthority(authorityName));
    }
}