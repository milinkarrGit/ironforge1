
package com.ironforge.repository;

import com.ironforge.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository
        extends JpaRepository<Message, Long> {

    List<Message> findByDestinataireId(Long destinataireId);

    List<Message> findByExpediteurId(Long expediteurId);

    List<Message> findByDestinataireIdAndLu(
            Long destinataireId, Boolean lu);
}


