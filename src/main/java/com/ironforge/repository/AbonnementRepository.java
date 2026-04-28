
package com.ironforge.repository;

import com.ironforge.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbonnementRepository
        extends JpaRepository<Abonnement, Long> {

    List<Abonnement> findByMembreId(Long membreId);

    Optional<Abonnement> findByMembreIdAndStatut(
            Long membreId, Abonnement.Statut statut);
}

