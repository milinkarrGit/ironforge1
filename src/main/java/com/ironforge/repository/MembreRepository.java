package com.ironforge.repository;

import com.ironforge.model.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MembreRepository
        extends JpaRepository<Membre, Long> {

    Optional<Membre> findByUtilisateurId(Long utilisateurId);

    Optional<Membre> findByUtilisateurEmail(String email);

    Optional<Membre> findByEmail(String email);
}