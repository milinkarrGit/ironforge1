
package com.ironforge.repository;

import com.ironforge.model.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PointsRepository
        extends JpaRepository<Points, Long> {

    Optional<Points> findByMembreId(Long membreId);
}

