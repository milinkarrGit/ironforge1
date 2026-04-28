package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.dto.RegisterRequest;
import com.ironforge.model.*;
import com.ironforge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UtilisateurRepository utilisateurRepository;
    private final MembreRepository membreRepository;
    private final CoachRepository coachRepository;
    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final ProgrammeRepository programmeRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== STATS =====
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getStats() {
        try {
            long totalUtilisateurs = utilisateurRepository.count();
            long totalMembres = membreRepository.count();
            long totalCoachs = coachRepository.count();
            long totalCommandes = commandeRepository.count();
            long totalProduits = produitRepository.count();

            String stats = String.format(
                    "Utilisateurs: %d | Membres: %d | Coachs: %d | Commandes: %d | Produits: %d",
                    totalUtilisateurs, totalMembres,
                    totalCoachs, totalCommandes, totalProduits
            );

            return ResponseEntity.ok(
                    ApiResponse.succes("Statistiques", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== UTILISATEURS =====
    @GetMapping("/utilisateurs")
    public ResponseEntity<ApiResponse> getAllUtilisateurs() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Utilisateurs récupérés",
                            utilisateurRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PutMapping("/utilisateurs/{id}/activer")
    public ResponseEntity<ApiResponse> activerCompte(
            @PathVariable Long id) {
        try {
            Utilisateur u = utilisateurRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));
            u.setActif(true);
            utilisateurRepository.save(u);
            return ResponseEntity.ok(
                    ApiResponse.succes("Compte activé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PutMapping("/utilisateurs/{id}/desactiver")
    public ResponseEntity<ApiResponse> desactiverCompte(
            @PathVariable Long id) {
        try {
            Utilisateur u = utilisateurRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));
            u.setActif(false);
            utilisateurRepository.save(u);
            return ResponseEntity.ok(
                    ApiResponse.succes("Compte désactivé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<ApiResponse> deleteUtilisateur(
            @PathVariable Long id) {
        try {
            utilisateurRepository.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.succes("Utilisateur supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== MEMBRES =====
    @GetMapping("/membres")
    public ResponseEntity<ApiResponse> getAllMembres() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Membres récupérés",
                            membreRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PostMapping("/membres/ajouter")
    public ResponseEntity<ApiResponse> ajouterMembre(
            @RequestBody RegisterRequest request) {
        try {
            if (utilisateurRepository
                    .existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur("Email déjà utilisé"));
            }

            Utilisateur utilisateur = Utilisateur.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(passwordEncoder.encode(
                            request.getMotDePasse()))
                    .telephone(request.getTelephone())
                    .role(Utilisateur.Role.MEMBRE)
                    .actif(true)
                    .build();

            utilisateurRepository.save(utilisateur);

            Membre membre = Membre.builder()
                    .utilisateur(utilisateur)
                    .points(0)
                    .niveau("DEBUTANT")
                    .build();

            membreRepository.save(membre);

            return ResponseEntity.ok(
                    ApiResponse.succes("Membre ajouté !", membre));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== COACHS =====
    @GetMapping("/coachs")
    public ResponseEntity<ApiResponse> getAllCoachs() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Coachs récupérés",
                            coachRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PostMapping("/coachs/ajouter")
    public ResponseEntity<ApiResponse> ajouterCoach(
            @RequestBody RegisterRequest request) {
        try {
            if (utilisateurRepository
                    .existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur("Email déjà utilisé"));
            }

            Utilisateur utilisateur = Utilisateur.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(passwordEncoder.encode(
                            request.getMotDePasse()))
                    .telephone(request.getTelephone())
                    .role(Utilisateur.Role.COACH)
                    .actif(true)
                    .build();

            utilisateurRepository.save(utilisateur);

            Coach coach = Coach.builder()
                    .utilisateur(utilisateur)
                    .specialite(request.getTelephone() != null ?
                            request.getTelephone() : "À définir")
                    .tarif(0.0)
                    .note(0.0)
                    .experience(0)
                    .build();

            coachRepository.save(coach);

            return ResponseEntity.ok(
                    ApiResponse.succes("Coach ajouté !", coach));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PutMapping("/coachs/{id}")
    public ResponseEntity<ApiResponse> modifierCoach(
            @PathVariable Long id,
            @RequestBody Coach coachUpdate) {
        try {
            Coach coach = coachRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Coach non trouvé"));

            if (coachUpdate.getSpecialite() != null)
                coach.setSpecialite(coachUpdate.getSpecialite());
            if (coachUpdate.getTarif() != null)
                coach.setTarif(coachUpdate.getTarif());
            if (coachUpdate.getExperience() != null)
                coach.setExperience(coachUpdate.getExperience());
            if (coachUpdate.getCertifications() != null)
                coach.setCertifications(coachUpdate.getCertifications());

            coachRepository.save(coach);
            return ResponseEntity.ok(
                    ApiResponse.succes("Coach modifié !", coach));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @DeleteMapping("/coachs/{id}")
    public ResponseEntity<ApiResponse> supprimerCoach(
            @PathVariable Long id) {
        try {
            Coach coach = coachRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Coach non trouvé"));
            coachRepository.delete(coach);
            return ResponseEntity.ok(
                    ApiResponse.succes("Coach supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== COMMANDES =====
    @GetMapping("/commandes")
    public ResponseEntity<ApiResponse> getAllCommandes() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Commandes récupérées",
                            commandeRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== PRODUITS =====
    @GetMapping("/produits")
    public ResponseEntity<ApiResponse> getAllProduits() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Produits récupérés",
                            produitRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== PROGRAMMES =====
    @GetMapping("/programmes")
    public ResponseEntity<ApiResponse> getAllProgrammes() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Programmes récupérés",
                            programmeRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // ===== SEANCES =====
    @GetMapping("/seances")
    public ResponseEntity<ApiResponse> getAllSeances() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.succes("Séances récupérées",
                            seanceRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }
}