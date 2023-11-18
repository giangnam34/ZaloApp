package com.essay.zaloapp.secruity;

import com.essay.zaloapp.domain.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String phoneNumber;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String phoneNumber, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        System.out.println("UserPrincipal: createUserPrincipal");
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                (long) user.getId(),
                user.getPhoneNumber(),
                user.getPassword(),
                authorities
        );
    }


    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
