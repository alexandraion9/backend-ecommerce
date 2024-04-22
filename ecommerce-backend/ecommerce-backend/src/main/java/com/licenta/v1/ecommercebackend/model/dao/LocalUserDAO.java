package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<LocalUser,Long> {
    Optional<LocalUser> findByEmailIgnoreCase(String email);
    Optional<LocalUser> findByUsernameIgnoreCase(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}
