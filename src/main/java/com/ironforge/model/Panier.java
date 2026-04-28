package com.ironforge.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "paniers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "total")
    private Double total = 0.0;

    @OneToOne
    @JoinColumn(name = "membre_id", unique = true)
    private Membre membre;

    @ManyToMany
    @JoinTable(
            name = "panier_produits",
            joinColumns = @JoinColumn(name = "panier_id"),
            inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private List<Produit> produits;

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
    }
}