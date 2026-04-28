
package com.ironforge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "programmes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Programme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duree_semaines")
    private Integer dureeSemaines;

    @Column(name = "niveau", length = 30)
    private String niveau;

    @Column(name = "jours_par_semaine")
    private Integer joursParSemaine;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonIgnoreProperties ({"exercices","membres","hibernateLazyInitializer"})
    private Coach coach;

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("programme")
    private List<Exercice> exercices;

    @ManyToMany
    @JoinTable(
            name = "membre_programmes",
            joinColumns = @JoinColumn(name = "programme_id"),
            inverseJoinColumns = @JoinColumn(name = "membre_id")
    )
    @JsonIgnoreProperties({"programmes","hibernateLazyInitializer"})
    private List<Membre> membres;

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
    }
}
