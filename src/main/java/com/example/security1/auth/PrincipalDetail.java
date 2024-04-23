package com.example.security1.auth;


import com.example.security1.model.User;
import java.util.Collection;
import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// security가 /login 주소 요청을 낚아채서 로그인을 진행시킴.
// 로그인을 진행이 완료가 되면 security session을 만들어줌. (Security ContextHolder)
// session에 들어갈 수 있는 object => Authentication type object
// Authentication 안에 User정보가 있어야 함
// User object type => UserDetails type object여야 함
// Security Session => Authentication => UserDetails(PrincipalDetail)
public class PrincipalDetail implements UserDetails {
    private User user; // composition

    public PrincipalDetail(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호의 유효기간이 지났니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트!! 1년동안 회원이 로그인을 안하면, 휴면으로 변경한다면
        return true;
    }
}
