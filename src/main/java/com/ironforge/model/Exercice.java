
package com.ironforge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "exercices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "series")
    private Integer series;

    @Column(name = "repetitions")
    private Integer repetitions;

    @Column(name = "duree_minutes")
    private Integer dureeMinutes;

    @Column(name = "categorie", length = 50)
    private String categorie;

    @ManyToOne
    @JoinColumn(name = "programme_id")
    @JsonIgnoreProperties({"exercices","membres"})
    private Programme programme;
}
