package com.ironforge.repository;

import com.ironforge.model.Commande;
import com.ironforge.model.Membre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByMembreOrderByDateCommandeDesc(Membre membre);
}