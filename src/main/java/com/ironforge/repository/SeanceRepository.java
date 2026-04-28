
package com.ironforge.repository;

import com.ironforge.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeanceRepository
        extends JpaRepository<Seance, Long> {

    List<Seance> findByMembreId(Long membreId);

    List<Seance> findByCoachId(Long coachId);

    List<Seance> findByStatut(Seance.Statut statut);

    List<Seance> findByDate(LocalDate date);
}