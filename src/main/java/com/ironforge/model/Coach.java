
package com.ironforge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "coachs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Utilisateur utilisateur;

    @Column(name = "specialite", length = 100)
    private String specialite;

    @Column(name = "tarif")
    private Double tarif;

    @Column(name = "disponibilite")
    private String disponibilite;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "certifications")
    private String certifications;

    @Column(name = "note")
    private Double note = 0.0;
}
