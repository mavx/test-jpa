package org.example.testjpa.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class SalesTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime postedDate;
    private String description;
    private Integer amountCents;
    private String status;
}
