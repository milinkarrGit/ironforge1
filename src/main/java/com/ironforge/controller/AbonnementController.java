package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.Abonnement;
import com.ironforge.model.Membre;
import com.ironforge.model.Utilisateur;
import com.ironforge.repository.AbonnementRepository;
import com.ironforge.repository.MembreRepository;
import com.ironforge.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
public class AbonnementController {

    private final AbonnementRepository abonnementRepository;
    private final MembreRepository membreRepository;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/souscrire")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> souscrire(
            @RequestBody Abonnement abonnement,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Utilisateur u = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));
            Membre membre = membreRepository
                    .findByUtilisateurId(u.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Membre non trouvé"));

            abonnement.setMembre(membre);
            abonnement.setStatut(Abonnement.Statut.ACTIF);
            abonnementRepository.save(abonnement);

            return ResponseEntity.ok(
                    ApiResponse.succes("Abonnement activé !", abonnement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @GetMapping("/mon-abonnement")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> getMonAbonnement(
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Utilisateur u = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));
            Membre membre = membreRepository
                    .findByUtilisateurId(u.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Membre non trouvé"));

            Optional<Abonnement> abonnement =
                    abonnementRepository.findByMembreIdAndStatut(
                            membre.getId(), Abonnement.Statut.ACTIF);

            if (abonnement.isPresent()) {
                return ResponseEntity.ok(
                        ApiResponse.succes("Abonnement trouvé",
                                abonnement.get()));
            } else {
                return ResponseEntity.ok(
                        ApiResponse.succes("Aucun abonnement actif", null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    @PutMapping("/resilier")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> resilier(
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Utilisateur u = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur non trouvé"));
            Membre membre = membreRepository
                    .findByUtilisateurId(u.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Membre non trouvé"));

            Optional<Abonnement> abonnement =
                    abonnementRepository.findByMembreIdAndStatut(
                            membre.getId(), Abonnement.Statut.ACTIF);

            if (abonnement.isPresent()) {
                abonnement.get().setStatut(Abonnement.Statut.RESILIE);
                abonnementRepository.save(abonnement.get());
                return ResponseEntity.ok(
                        ApiResponse.succes("Abonnement résilié"));
            }
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur("Aucun abonnement actif"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }
}