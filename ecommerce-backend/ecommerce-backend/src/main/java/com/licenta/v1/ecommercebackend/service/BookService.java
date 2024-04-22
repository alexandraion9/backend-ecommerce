package com.licenta.v1.ecommercebackend.service;

import com.licenta.v1.ecommercebackend.model.Author;
import com.licenta.v1.ecommercebackend.model.Inventory;
import com.licenta.v1.ecommercebackend.model.Publisher;
import com.licenta.v1.ecommercebackend.model.Book;
import com.licenta.v1.ecommercebackend.model.dao.AuthorDAO;
import com.licenta.v1.ecommercebackend.model.dao.BookDAO;
import com.licenta.v1.ecommercebackend.model.dao.PublisherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private PublisherDAO publisherDAO;

    @Autowired
    private BookDAO bookDAO;

    @Transactional
    public void saveAuthor(Author author) { authorDAO.save(author); }

    @Transactional
    public void savePublisher(Publisher publisher) { publisherDAO.save(publisher); }

    @Transactional
    public Book saveBook(Book book) {
        if(book.getInventory() == null){
            book.setInventory(new Inventory());
        }
        book.getInventory().setBook(book);
        return bookDAO.save(book);
    }

    //Motor de cautare

    public List<Book> searchBooksByTitle(String keyword) {
        return bookDAO.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Book> searchBooksByAuthor(String keyword) {
        return bookDAO.findByAuthorFirstNameContainingIgnoreCaseAndAuthorLastNameContainingIgnoreCase(keyword,keyword);
    }

    public List<Book> searchBooksByGenre(String keyword) {
        return bookDAO.findByGenreContainingIgnoreCase(keyword);
    }

    //Sortare produse
    public List<Book> getAllBooksSortedByTitle() {
        return bookDAO.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Book> getAllBooksSortedByPrice() {
        return bookDAO.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }
}

