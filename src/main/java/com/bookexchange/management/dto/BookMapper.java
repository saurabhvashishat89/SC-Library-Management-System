package com.bookexchange.management.dto;

import com.bookexchange.management.entity.Book;

public class BookMapper {
    public static BookDO toBookDO(Book book) {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(book.getTitle());
        bookDO.setAuthor(book.getAuthor());
        bookDO.setBookcondition(book.getBookcondition());
        bookDO.setAvailabilityStatus(book.getAvailabilityStatus());
        bookDO.setUserId(book.getId());
        bookDO.setId(book.getId());
        // Map other fields as needed
        return bookDO;
    }
}

