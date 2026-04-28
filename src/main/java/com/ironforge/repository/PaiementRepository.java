package com.ironforge.repository;

import com.ironforge.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository
        extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByCommandeId(Long commandeId);

    Optional<Paiement> findByAbonnementId(Long abonnementId);

    List<Paiement> findByStatut(Paiement.Statut statut);

    Optional<Paiement> findByReferenceStripe(
            String referenceStripe);
}
