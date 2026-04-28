
package com.ironforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "abonnements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "duree_jours")
    private Integer dureeJours = 30;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @NotBlank(message = "Le type est obligatoire")
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @NotNull(message = "Le prix est obligatoire")
    @Column(name = "prix", nullable = false)
    private Double prix;


    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.ACTIF;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    public enum Statut {
        ACTIF, EXPIRE, RESILIE
    }
}