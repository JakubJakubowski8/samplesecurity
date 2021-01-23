package com.jakub.samplesecurity;

import com.jakub.samplesecurity.model.Right;
import com.jakub.samplesecurity.model.RightName;
import com.jakub.samplesecurity.model.Role;
import com.jakub.samplesecurity.model.RoleName;
import com.jakub.samplesecurity.model.User;
import com.jakub.samplesecurity.repository.RightRepository;
import com.jakub.samplesecurity.repository.RoleRepository;
import com.jakub.samplesecurity.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
  public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RightRepository rightRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserRepository userRepository,
                           RoleRepository roleRepository,
                           RightRepository rightRepository,
                           PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
      this.rightRepository = rightRepository;
      this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

      if (alreadySetup) {
        return;
      }

      final Right createRight = createRightIfNotFound(RightName.ROLE_CREATE_USER);
      final Right updateRight = createRightIfNotFound(RightName.ROLE_UPDATE_USER);
      final Right deleteRight = createRightIfNotFound(RightName.ROLE_DELETE_USER);
      final Right listUserRight = createRightIfNotFound(RightName.ROLE_LIST_USER);

      final Set<Right> adminRights = new HashSet<>(Arrays.asList(createRight,
          updateRight, deleteRight, listUserRight));

      final Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN.name(), adminRights);

      createUserIfNotFound( "admin", "password", new HashSet<>(Collections.singleton(adminRole)));

      alreadySetup = true;
    }

    @Transactional
    private Right createRightIfNotFound(final RightName name) {

      if (!rightRepository.existsByName(name)) {
        Right right = new Right(name);
        right = rightRepository.save(right);
        return right;
      }
      return null;
    }

    @Transactional
    private Role createRoleIfNotFound(final String name, final Set<Right> rights) {

      if (!roleRepository.existsByName(name)) {
        Role role = new Role();
        role.setName(name);
        role.setRight(rights);
        role = roleRepository.save(role);
        return role;
      }
      return null;
    }

    @Transactional
    private void createUserIfNotFound(final String username, final String password,
                                      final Set<Role> roles) {

      if (!userRepository.existsByUsername(username)) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
      }
    }
  }