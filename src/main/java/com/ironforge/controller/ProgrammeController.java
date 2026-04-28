package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.Programme;
import com.ironforge.repository.ProgrammeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/programmes")
@RequiredArgsConstructor
public class ProgrammeController {

    private final ProgrammeRepository programmeRepository;

    // GET /api/programmes/public — PUBLIC
    @GetMapping("/public")
    public ResponseEntity<ApiResponse> getAllPublic() {
        try {
            List<Programme> programmes =
                    programmeRepository.findAll();
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Programmes récupérés", programmes)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/programmes/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> getProgrammeById(
            @PathVariable Long id) {
        try {
            Programme programme = programmeRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Programme non trouvé")
                    );
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Programme récupéré", programme)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/programmes/niveau/{niveau}
    @GetMapping("/niveau/{niveau}")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> getByNiveau(
            @PathVariable String niveau) {
        try {
            List<Programme> programmes =
                    programmeRepository.findByNiveau(niveau);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Programmes par niveau", programmes)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // POST /api/programmes — COACH et ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('COACH','ADMIN')")
    public ResponseEntity<ApiResponse> createProgramme(
            @RequestBody Programme programme) {
        try {
            Programme saved =
                    programmeRepository.save(programme);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Programme créé", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // PUT /api/programmes/{id} — COACH et ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COACH','ADMIN')")
    public ResponseEntity<ApiResponse> updateProgramme(
            @PathVariable Long id,
            @RequestBody Programme programmeUpdate) {
        try {
            Programme programme = programmeRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Programme non trouvé")
                    );

            programme.setNom(programmeUpdate.getNom());
            programme.setDescription(
                    programmeUpdate.getDescription());
            programme.setNiveau(programmeUpdate.getNiveau());
            programme.setDureeSemaines(
                    programmeUpdate.getDureeSemaines());
            programme.setJoursParSemaine(
                    programmeUpdate.getJoursParSemaine());

            Programme saved =
                    programmeRepository.save(programme);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Programme mis à jour", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // DELETE /api/programmes/{id} — ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProgramme(
            @PathVariable Long id) {
        try {
            programmeRepository.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.succes("Programme supprimé")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }
}