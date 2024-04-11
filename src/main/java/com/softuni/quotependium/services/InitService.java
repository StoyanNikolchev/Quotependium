package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.domain.enums.UserRoleEnum;
import com.softuni.quotependium.repositories.BookRepository;
import com.softuni.quotependium.repositories.UserRepository;
import com.softuni.quotependium.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class InitService {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;
    private final DataSource dataSource;
    @Autowired
    public InitService(UserRoleRepository userRoleRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, BookRepository bookRepository, DataSource dataSource){

        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void init() {
        initRoles();
        initAdmin();
        initDataFile();
    }

    private void initDataFile() {
        if (this.bookRepository.count() == 0) {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("data.sql"));
            resourceDatabasePopulator.execute(dataSource);
        }
    }

    private void initAdmin() {
        if (this.userRepository.count() == 0) {
            UserEntity admin = new UserEntity()
                    .setEmail("admin@admin.com")
                    .setFullName("Admin Adminov")
                    .setUsername("Admin")
                    .setPassword(this.passwordEncoder.encode("turboUnbreakablePassword"))
                    .setRoles(userRoleRepository.findAll());

            this.userRepository.save(admin);
        }
    }

    private void initRoles() {
        if(this.userRoleRepository.count() == 0) {
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRoleEnum.USER);
            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRoleEnum.ADMINISTRATOR);

            this.userRoleRepository.save(userRole);
            this.userRoleRepository.save(adminRole);
        }
    }
}