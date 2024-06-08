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

    public Author findAuthorByFirstNameAndLastName(String firstName, String lastName) {
        return authorDAO.findAuthorByFirstNameAndLastName(firstName, lastName);
    }

    public Publisher findPublisherByName(String name) {
        return publisherDAO.findByName(name);
    }

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

    public List<Book> getAllBooks() {return bookDAO.findAll();}

    public List<Book> searchBooksByTitle(String keyword) {
        return bookDAO.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Book> searchBooksByAuthor(String keyword) { return bookDAO.findByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(keyword,keyword);}

    public List<Book> searchBooksByGenre(String keyword) {
        return bookDAO.findByGenreContainingIgnoreCase(keyword);
    }

    public List<Book> searchBooksByPublisher(String keyword) { return bookDAO.findByPublisherNameContainingIgnoreCase(keyword);}

    //Sortare produse

    public List<Book> getAllBooksSortedByTitle() {
        return bookDAO.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Book> getAllBooksSortedByPrice() {
        return bookDAO.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    public List<Book> getAllBooksSortedByPages() { return bookDAO.findAll(Sort.by(Sort.Direction.ASC, "pages"));}





}

