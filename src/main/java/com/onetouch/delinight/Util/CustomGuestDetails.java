package com.onetouch.delinight.Util;

import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomGuestDetails implements UserDetails {

    private final String phone;
    @Getter
    private final GuestEntity guestEntity;

    private int changeStatus = 0;


    public CustomGuestDetails(GuestEntity guestEntity){
        this.phone = guestEntity.getPhone();
        this.guestEntity = guestEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return guestEntity.getPassword();

    }

    @Override
    public String getUsername() {
        return phone;
    }

    public Integer getChangeStatus(){
        return this.changeStatus;
    }
    public void setChangeStatus(int count){
        this.changeStatus = count;
    }
}
