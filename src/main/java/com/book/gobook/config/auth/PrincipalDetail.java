package com.book.gobook.config.auth;

import com.book.gobook.model.Members;
import jakarta.persistence.metamodel.IdentifiableType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetail implements UserDetails {
    private Members members;

    public Members getMembers() {
        return this.members;
    }

    public PrincipalDetail(Members members) {
        this.members = members;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return members.getPwd();
    }

    @Override
    public String getUsername() {
        return members.getId();
    }

    public int getNum() {
        return members.getNum();
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


    public Members getId() {
        return members;
    }
}