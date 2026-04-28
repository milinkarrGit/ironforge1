package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.Membre;
import com.ironforge.model.Seance;
import com.ironforge.model.Utilisateur;
import com.ironforge.repository.CoachRepository;
import com.ironforge.repository.MembreRepository;
import com.ironforge.repository.SeanceRepository;
import com.ironforge.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seances")
@CrossOrigin(origins = "http://localhost:63342")
@RequiredArgsConstructor
public class SeanceController {

    private final SeanceRepository seanceRepository;
    private final MembreRepository membreRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CoachRepository coachRepository;


    @GetMapping("/mes-seances-coach")
    @PreAuthorize("hasAnyRole('COACH','ADMIN')")
    public ResponseEntity<ApiResponse> getSeancesCoach(
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));

            List<Seance> seances = seanceRepository
                    .findByCoachId(
                            coachRepository.findByUtilisateurId(
                                            utilisateur.getId())
                                    .orElseThrow().getId()
                    );

            return ResponseEntity.ok(
                    ApiResponse.succes("Séances coach", seances));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // GET /api/seances/disponibles — PUBLIC
    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse> getDisponibles() {
        try {
            List<Seance> seances = seanceRepository
                    .findByStatut(Seance.Statut.DISPONIBLE);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Séances disponibles", seances)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/seances/mes-seances
    @GetMapping("/mes-seances")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> getMesSeances(
            Authentication authentication) {
        try {
            String email = authentication.getName();

            Utilisateur utilisateur = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé")
                    );

            Membre membre = membreRepository
                    .findByUtilisateurId(utilisateur.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Membre non trouvé")
                    );

            List<Seance> seances = seanceRepository
                    .findByMembreId(membre.getId());

            return ResponseEntity.ok(
                    ApiResponse.succes("Mes séances", seances)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // POST /api/seances/reserver/{id}
    @PostMapping("/reserver/{id}")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse> reserverSeance(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = authentication.getName();

            Utilisateur utilisateur = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));


            // Chercher le membre
            Optional<Membre> membreOpt = membreRepository
                    .findByUtilisateurId(utilisateur.getId());

            if (membreOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur(
                                "Profil membre non trouvé"));
            }

            Membre membre = membreOpt.get();

            Seance seance = seanceRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Séance non trouvée"));

            if (seance.getStatut() != Seance.Statut.DISPONIBLE) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur("Séance non disponible"));
            }

            seance.setMembre(membre);
            seance.setStatut(Seance.Statut.RESERVEE);
            seanceRepository.save(seance);

            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Séance réservée avec succès !", seance));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }


    // PUT /api/seances/annuler/{id}
    @PutMapping("/annuler/{id}")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> annulerSeance(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            Seance seance = seanceRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Séance non trouvée")
                    );

            seance.setStatut(Seance.Statut.ANNULEE);
            seance.setMembre(null);
            seanceRepository.save(seance);

            return ResponseEntity.ok(
                    ApiResponse.succes("Séance annulée")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // POST /api/seances — COACH et ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('COACH','ADMIN')")
    public ResponseEntity<ApiResponse> createSeance(
            @RequestBody Seance seance) {
        try {
            Seance saved = seanceRepository.save(seance);
            return ResponseEntity.ok(
                    ApiResponse.succes("Séance créée", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }
}