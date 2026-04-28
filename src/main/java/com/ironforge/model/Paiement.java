
package com.ironforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le montant est obligatoire")
    @Column(name = "montant", nullable = false)
    private Double montant;

    @Column(name = "date_transaction")
    private LocalDateTime dateTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "methode_paiement", length = 50)
    private String methodePaiement;

    @Column(name = "reference_stripe")
    private String referenceStripe;

    @OneToOne
    @JoinColumn(name = "commande_id", unique = true)
    private Commande commande;

    @OneToOne
    @JoinColumn(name = "abonnement_id", unique = true)
    private Abonnement abonnement;

    @PrePersist
    public void prePersist() {
        dateTransaction = LocalDateTime.now();
    }

    public enum Statut {
        EN_ATTENTE, REUSSI, ECHOUE, REMBOURSE
    }
}
