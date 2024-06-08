package com.licenta.v1.ecommercebackend.api.controller;

import com.licenta.v1.ecommercebackend.model.Author;
import com.licenta.v1.ecommercebackend.model.Book;
import com.licenta.v1.ecommercebackend.model.Publisher;
import com.licenta.v1.ecommercebackend.model.dao.AuthorDAO;
import com.licenta.v1.ecommercebackend.model.dao.BookDAO;
import com.licenta.v1.ecommercebackend.model.dao.PublisherDAO;
import com.licenta.v1.ecommercebackend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/books")
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private PublisherDAO publisherDAO;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book)  {

        Author existingAuthor = bookService.findAuthorByFirstNameAndLastName(book.getAuthor().getFirstName(), book.getAuthor().getLastName());
        if (existingAuthor != null) {
            book.setAuthor(existingAuthor);
        } else {
            bookService.saveAuthor(book.getAuthor());
        }

        Publisher existingPublisher = bookService.findPublisherByName(book.getPublisher().getName());
        if (existingPublisher != null) {
            book.setPublisher(existingPublisher);
        } else {
            bookService.savePublisher(book.getPublisher());
        }

        bookService.saveBook(book);

        return ResponseEntity.ok(book);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> allBooks = bookDAO.findAll();
        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookDAO.findById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book){
        Optional<Book> existingBook = bookDAO.findById(id);
        if (existingBook.isPresent()){
            book.setId(existingBook.get().getId());
            Book updateBook = bookDAO.save(book);
            return ResponseEntity.ok(updateBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookDAO.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (book.getAuthor() != null) {
                Author author = book.getAuthor();
                author.getBooks().remove(book);
                authorDAO.save(author);
            }

            if (book.getPublisher() != null) {
                Publisher publisher = book.getPublisher();
                publisher.getBooks().remove(book);
                publisherDAO.save(publisher);
            }
            bookDAO.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooksByTitle(keyword);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooksByAuthor(keyword);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/search/genre")
    public ResponseEntity<List<Book>> searchBooksByGenre(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooksByGenre(keyword);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/search/publisher")
    public ResponseEntity<List<Book>> searchBooksByPublisher(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooksByPublisher(keyword);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/sort/title")
    public ResponseEntity<List<Book>> getAllBooksSortedByTitle() {
        List<Book> books = bookService.getAllBooksSortedByTitle();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/sort/price")
    public ResponseEntity<List<Book>> getAllBooksSortedByPrice() {
        List<Book> books = bookService.getAllBooksSortedByPrice();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/sort/pages")
    public ResponseEntity<List<Book>> getAllBooksSortedByPages() {
        List<Book> books = bookService.getAllBooksSortedByPages();
        return ResponseEntity.ok().body(books);
    }

}
