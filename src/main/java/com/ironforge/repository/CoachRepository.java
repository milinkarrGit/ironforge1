
package com.ironforge.repository;

import com.ironforge.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CoachRepository
        extends JpaRepository<Coach, Long> {


    Optional<Coach> findByUtilisateurId(Long id);
}