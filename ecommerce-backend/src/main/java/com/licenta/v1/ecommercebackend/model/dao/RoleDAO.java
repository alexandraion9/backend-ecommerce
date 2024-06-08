package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.ERole;
import com.licenta.v1.ecommercebackend.model.Role;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleDAO extends ListCrudRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
