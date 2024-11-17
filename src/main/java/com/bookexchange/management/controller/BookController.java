package com.bookexchange.management.controller;

import com.bookexchange.management.dto.BookDO;
import com.bookexchange.management.entity.Book;
import com.bookexchange.management.entity.User;
import com.bookexchange.management.service.BookService;
import com.bookexchange.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    @Autowired
     UserService userService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Add a new book
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookDO book) {
        Book bookEntity=new Book();
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setAvailabilityStatus(book.getAvailabilityStatus());
        bookEntity.setBookcondition(book.getBookcondition());
        bookEntity.setGenre(book.getGenre());
        Optional<User> user=userService.findById(book.getUserId());
//        user.get().setId(book.getUserId());
        bookEntity.setUser(user.orElse(null));
//        bookEntity.getUser().setId(book.getUserId());

        return ResponseEntity.ok(bookService.addBook(bookEntity));
    }

    // Get books by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Book>> getBooksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookService.getBooksByUser(userId));
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookService.getBookById(id)
                .map(book -> {
                    updatedBook.setId(id);
                    return ResponseEntity.ok(bookService.updateBook(updatedBook));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // Search books
    @GetMapping("/search")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Page<BookDO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean availabilityStatus,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.searchBooks(title, author, genre, availabilityStatus, location, page, size));
    }
}

