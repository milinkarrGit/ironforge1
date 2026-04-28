
package com.ironforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "image")
    private String image;

    @Column(name = "promotion")
    private Double promotion = 0.0;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
}