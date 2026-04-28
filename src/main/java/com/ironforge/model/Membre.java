package com.ironforge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "membres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Utilisateur utilisateur;

    @Column(name = "points")
    private Integer points = 0;

    @Column(unique = true)
    private String email;

    @Column(name = "niveau", length = 30)
    private String niveau = "DEBUTANT";

    @Column(name = "objectif", length = 100)
    private String objectif;

    @Column(name = "poids")
    private Double poids;

    @Column(name = "taille")
    private Double taille;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
}