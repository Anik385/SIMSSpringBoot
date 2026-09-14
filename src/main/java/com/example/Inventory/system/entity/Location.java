package com.example.Inventory.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aisle;
    private String shelf;
    private String bin;
    private String description;
}
