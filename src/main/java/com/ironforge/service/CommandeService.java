package com.ironforge.service;

import com.ironforge.model.Commande;
import com.ironforge.model.Membre;
import com.ironforge.repository.CommandeRepository;
import com.ironforge.repository.MembreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final MembreRepository membreRepository;
    private final com.ironforge.security.JwtService jwtService;

    public List<Commande> getCommandesByUserToken(String token) {

        // 1. Nettoyer le token
        String jwt = token.replace("Bearer ", "");

        // 2. Extraire email/username
        String email = jwtService.extractUsername(jwt);

        // 3. Trouver le membre connecté
        Membre membre = membreRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Membre introuvable"));

        // 4. Retourner ses commandes
        return commandeRepository.findByMembreOrderByDateCommandeDesc(membre);
    }
}
