package com.ironforge.service;

import com.ironforge.dto.LoginRequest;
import com.ironforge.dto.LoginResponse;
import com.ironforge.dto.RegisterRequest;
import com.ironforge.model.Membre;
import com.ironforge.model.Utilisateur;
import com.ironforge.repository.MembreRepository;
import com.ironforge.repository.UtilisateurRepository;
import com.ironforge.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final MembreRepository membreRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // CONNEXION
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur non trouvé")
                );

        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRole().name()
        );

        return new LoginResponse(
                token,
                utilisateur.getEmail(),
                utilisateur.getRole().name(),
                utilisateur.getNom(),
                utilisateur.getPrenom()
        );
    }

    // INSCRIPTION
    @Transactional
    public String register(RegisterRequest request) {

        if (utilisateurRepository
                .existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
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

        utilisateur = utilisateurRepository
                .saveAndFlush(utilisateur);

        Membre membre = Membre.builder()
                .utilisateur(utilisateur)
                .points(0)
                .niveau("DEBUTANT")
                .build();

        membreRepository.saveAndFlush(membre);

        return "Inscription réussie !";
    }
}