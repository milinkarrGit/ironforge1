
package com.ironforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    public enum Statut {
        EN_ATTENTE, CONFIRMEE, LIVREE, ANNULEE
    }

    @Column(name = "date_commande")
    private LocalDateTime dateCommande;

    @NotNull(message = "Le total est obligatoire")
    @Column(name = "total", nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "adresse_livraison")
    private String adresseLivraison;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    @ManyToMany
    @JoinTable(
            name = "commande_produits",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private List<Produit> produits;

    @PrePersist
    public void prePersist() {
        dateCommande = LocalDateTime.now();
    }


}