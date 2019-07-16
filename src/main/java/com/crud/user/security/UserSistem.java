package com.crud.user.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserSistem extends User {

    private static final long serialVersionUID = 1L;

    private com.crud.user.model.User user;

    public UserSistem(com.crud.user.model.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassord(), authorities);
        this.user = user;
    }

    public com.crud.user.model.User getUser() {
        return user;
    }
}
