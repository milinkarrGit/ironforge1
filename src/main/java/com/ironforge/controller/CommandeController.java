package com.ironforge.controller;

import com.ironforge.model.Commande;
import com.ironforge.service.CommandeService;
import com.ironforge.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping("/mes-commandes")
    public ResponseEntity<ApiResponse> getMesCommandes(
            @RequestHeader("Authorization") String token
    ) {


            // Service récupère les commandes de l'utilisateur connecté
            try {
                List<Commande> commandes =
                        commandeService.getCommandesByUserToken(token);

                return ResponseEntity.ok(
                         ApiResponse.succes("Commandes recuperees", commandes)
                );

            } catch (Exception e) {
                return ResponseEntity.status(500)
                        .body(new ApiResponse(false, "Erreur serveur : " + e.getMessage(), null));
            }

    }
}

