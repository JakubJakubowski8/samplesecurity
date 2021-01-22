package com.jakub.samplesecurity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.model.RoleName;
import com.jakub.samplesecurity.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
  private Long id;

  private String username;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public static UserPrincipal create(User user) {

    Set<String> rights = getRights(user.getRoles());

    if (user.getRoleStringNames().contains(RoleName.ROLE_ADMIN.name())) {
      rights.add(RoleName.ROLE_ADMIN.name());
    }

    List<GrantedAuthority> authorities =
        rights.stream().map(SimpleGrantedAuthority::new
    ).collect(Collectors.toList());

    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        authorities
    );
  }

  private static Set<String> getRights(Set<Role> roles) {

    Set<String> rights = new HashSet<>();
    roles.forEach(role -> rights.addAll(
        role.getRight().stream().map(right -> right.getName().name()).collect(Collectors.toList()))
    );
    return rights;

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserPrincipal that = (UserPrincipal) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }
}
