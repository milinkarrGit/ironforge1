
package com.ironforge.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Points {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solde")
    private Integer solde = 0;

    @Column(name = "total_gagne")
    private Integer totalGagne = 0;

    @Column(name = "total_depense")
    private Integer totalDepense = 0;

    @Column(name = "date_maj")
    private LocalDateTime dateMaj;

    @OneToOne
    @JoinColumn(name = "membre_id", unique = true)
    private Membre membre;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        dateMaj = LocalDateTime.now();
    }
}
