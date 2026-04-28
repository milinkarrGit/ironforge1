

package com.ironforge.repository;

import com.ironforge.model.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExerciceRepository
        extends JpaRepository<Exercice, Long> {

    List<Exercice> findByProgrammeId(Long programmeId);

    List<Exercice> findByCategorie(String categorie);
}