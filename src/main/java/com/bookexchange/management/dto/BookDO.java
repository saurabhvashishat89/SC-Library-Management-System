package com.bookexchange.management.dto;

import com.bookexchange.management.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDO {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String bookcondition;
    private Boolean availabilityStatus;
    private long userId; // Assuming User entity exists for user profiles

}
