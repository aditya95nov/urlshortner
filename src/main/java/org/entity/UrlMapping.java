package org.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "url_mapping",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "shortCode"),
                @UniqueConstraint(columnNames = "originalUrl")
        }
)
@Getter
@Setter
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;

    private String shortCode;

    private LocalDateTime createdAt;

}