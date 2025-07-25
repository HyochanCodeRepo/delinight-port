package com.onetouch.delinight.Util;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Entity.MembersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {

    private final String email;
    private final Role role;
    @Getter
    private final MembersEntity membersEntity;

    private int awaitingCount = 0;


    public MemberDetails(MembersEntity membersEntity){
        this.email = membersEntity.getEmail();
        this.role = membersEntity.getRole();
        this.membersEntity = membersEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ role));
    }

    @Override
    public String getPassword() {
        return membersEntity.getPassword();

    }

    @Override
    public String getUsername() {
        return email;
    }

    public Integer getAwaitingCount(){
        return this.awaitingCount;
    }
    public void setAwaitingCount(int count){
        this.awaitingCount = count;
    }
}
