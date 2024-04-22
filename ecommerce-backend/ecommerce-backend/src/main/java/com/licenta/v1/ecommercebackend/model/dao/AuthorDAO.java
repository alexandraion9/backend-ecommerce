package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.Author;
import org.springframework.data.repository.ListCrudRepository;

import java.awt.*;

public interface AuthorDAO extends ListCrudRepository<Author, List> {

}
