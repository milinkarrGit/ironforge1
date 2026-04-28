
package com.ironforge.repository;

import com.ironforge.model.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgrammeRepository
        extends JpaRepository<Programme, Long> {

    List<Programme> findByNiveau(String niveau);

    List<Programme> findByCoachId(Long coachId);

    List<Programme> findByMembresId(Long membreId);
}
