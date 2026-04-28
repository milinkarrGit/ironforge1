package com.ironforge.service;

import com.ironforge.model.*;
import com.ironforge.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;

    // 🔹 Récupérer tous les coachs
    public List<Coach> getAllCoachs() {
        return coachRepository.findAll();
    }

    // 🔹 Récupérer un coach par ID
    public Coach getCoachById(Long id) {
        Optional<Coach> coach = coachRepository.findById(id);
        return coach.orElse(null);
    }

    // 🔹 Créer un coach
    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    // 🔹 Modifier un coach
    public Coach updateCoach(Long id, Coach coach) {
        Optional<Coach> existing = coachRepository.findById(id);

        if (existing.isEmpty()) return null;

        Coach c = existing.get();

        // 🔥 adapte selon ton entity
        c.setSpecialite(coach.getSpecialite());
        c.setNote(coach.getNote());
        c.setUtilisateur(coach.getUtilisateur());

        return coachRepository.save(c);
    }

    // 🔹 Supprimer un coach
    public void deleteCoach(Long id) {
        coachRepository.deleteById(id);
    }

    // 🔥 IMPORTANT POUR TON DASHBOARD
    public List<Coach> getAllCoachsPublic() {
        return coachRepository.findAll();
    }
}