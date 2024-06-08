package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BookDAO extends ListCrudRepository<Book,Long> {

    List<Book> findByTitleContainingIgnoreCase(String keyword);

    List<Book> findByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Book> findByGenreContainingIgnoreCase(String keyword);

    List<Book> findByPublisherNameContainingIgnoreCase(String keyword);

    List<Book> findAll(Sort sort);

}
