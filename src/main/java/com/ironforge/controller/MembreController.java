package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.Membre;
import com.ironforge.model.Utilisateur;
import com.ironforge.repository.MembreRepository;
import com.ironforge.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membre")
@RequiredArgsConstructor
public class MembreController {

    private final MembreRepository membreRepository;
    private final UtilisateurRepository utilisateurRepository;

    // GET /api/membre/profil
    @GetMapping("/profil")
    @PreAuthorize("hasAnyRole('MEMBRE', 'COACH', 'ADMIN')")
    public ResponseEntity<ApiResponse> getProfil(
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
                            new RuntimeException("Profil membre non trouvé")
                    );

            return ResponseEntity.ok(
                    ApiResponse.succes("Profil récupéré", membre)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/membre/points
    @GetMapping("/points")
    @PreAuthorize("hasAnyRole('MEMBRE', 'COACH', 'ADMIN')")
    public ResponseEntity<ApiResponse> getPoints(
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

            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Points récupérés", membre.getPoints())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // PUT /api/membre/profil
    @PutMapping("/profil")
    @PreAuthorize("hasAnyRole('MEMBRE', 'COACH', 'ADMIN')")
    public ResponseEntity<ApiResponse> updateProfil(
            Authentication authentication,
            @RequestBody Membre membreUpdate) {
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

            membre.setObjectif(membreUpdate.getObjectif());
            membre.setPoids(membreUpdate.getPoids());
            membre.setTaille(membreUpdate.getTaille());
            membre.setDateNaissance(
                    membreUpdate.getDateNaissance());

            membreRepository.save(membre);

            return ResponseEntity.ok(
                    ApiResponse.succes("Profil mis à jour", membre)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }
}