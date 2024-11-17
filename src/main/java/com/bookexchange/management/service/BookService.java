package com.bookexchange.management.service;

import com.bookexchange.management.dto.BookDO;
import com.bookexchange.management.dto.BookMapper;
import com.bookexchange.management.entity.Book;
import com.bookexchange.management.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getBooksByUser(Long userId) {
        return bookRepository.findByUserId(userId);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<BookDO> searchBooks(String title, String author, String genre, Boolean availabilityStatus, String location, int page, int size) {
        Specification<Book> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null && !author.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }

        if (genre != null && !genre.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("genre"), genre));
        }

        if (availabilityStatus != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("availabilityStatus"), availabilityStatus));
        }

        if (location != null && !location.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }


           Page<Book> pageBook=     bookRepository.findAll(spec, PageRequest.of(page, size));
        Page<BookDO> pageBookDO = pageBook.map(BookMapper::toBookDO);

        return pageBookDO;
    }
}

