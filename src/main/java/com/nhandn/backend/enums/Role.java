package com.nhandn.backend.enums;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;

import java.util.List;



@RequiredArgsConstructor
public enum Role {


  TEACHER,
  STUDENT;



  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
  public static List<SimpleGrantedAuthority> getAllAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role: values()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    return authorities;
  }
}
