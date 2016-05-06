package com.auth;



import org.springframework.security.core.authority.AuthorityUtils;

import com.model.Role;
import com.model.WSUser;



public class TokenUser extends org.springframework.security.core.userdetails.User {
    private WSUser user;

    public TokenUser(WSUser user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public WSUser getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
