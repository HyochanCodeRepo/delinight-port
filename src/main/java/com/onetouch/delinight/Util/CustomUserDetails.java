package com.onetouch.delinight.Util;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String email;
    @Getter
    private final UsersEntity usersEntity;

    private int changeStatus = 0;


    public CustomUserDetails(UsersEntity usersEntity){
        this.email = usersEntity.getEmail();
        this.usersEntity = usersEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return usersEntity.getPassword();

    }

    @Override
    public String getUsername() {
        return email;
    }

    public Integer getChangeStatus(){
        return this.changeStatus;
    }
    public void setChangeStatus(int count){
        this.changeStatus = count;
    }
}
