package com.transempiric.webfluxTemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;
    private List<String> roles = new ArrayList<String>();
    private final boolean enabled;
    private final Date lastPasswordResetDate;

    @JsonCreator(mode=JsonCreator.Mode.PROPERTIES)
    public User(
            ObjectId id,
            String username,
            String firstname,
            String lastname,
            String email,
            String password, List<String> roles,
            boolean enabled,
            Date lastPasswordResetDate
    ) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.roles = roles;
    }

    //@JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //@JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //@JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //@JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList(this.roles.size());
        for(String role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    //@JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}