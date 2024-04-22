package com.licenta.v1.ecommercebackend.api.controller;

import com.licenta.v1.ecommercebackend.model.Book;
import com.licenta.v1.ecommercebackend.model.dao.BookDAO;
import com.licenta.v1.ecommercebackend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/books")
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookService bookService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book)  {

        if (book.getAuthor().getId() == null) {
            bookService.saveAuthor(book.getAuthor());
        }
        if (book.getPublisher().getId() == null) {
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
        bookDAO.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
