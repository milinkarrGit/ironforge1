
package com.ironforge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "seances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date est obligatoire")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.DISPONIBLE;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "calories_brulees")
    private Integer caloriesBrulees;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","seances"})
    private Coach coach;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","seances"})
    private Membre membre;

    public enum Statut {
        DISPONIBLE, RESERVEE, ANNULEE
    }
}