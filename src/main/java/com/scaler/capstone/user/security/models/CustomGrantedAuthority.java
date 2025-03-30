package com.scaler.capstone.user.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scaler.capstone.user.models.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
@NoArgsConstructor //--> Required for token creation
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName().toString();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
