package com.ironforge.security;

import com.ironforge.model.Utilisateur;
import com.ironforge.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class UserDetailsServiceImpl
        implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // ÉTAPE 1 — Chercher l'utilisateur dans MySQL
        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utilisateur non trouvé : " + email
                        )
                );

        // ÉTAPE 2 — Vérifier que le compte est actif
        if (!utilisateur.getActif()) {
            throw new UsernameNotFoundException(
                    "Compte désactivé : " + email
            );
        }

        // ÉTAPE 3 — Créer l'objet UserDetails
        return User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .authorities(List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + utilisateur.getRole().name()
                        )
                ))
                .build();
    }
}

