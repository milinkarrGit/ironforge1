
package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.*;
import com.ironforge.repository.*;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paiement")
@RequiredArgsConstructor
public class PaiementController {

    private final UtilisateurRepository utilisateurRepository;
    private final MembreRepository membreRepository;
    private final CommandeRepository commandeRepository;
    private final AbonnementRepository abonnementRepository;

    @Value("${stripe.public.key}")
    private String publicKey;

    // Récupérer clé publique
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publicKey", publicKey);
        return ResponseEntity.ok(config);
    }

    // Créer PaymentIntent pour boutique
    @PostMapping("/create-payment-intent")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> createPaymentIntent(
            @RequestBody Map<String, Object> body,
            Authentication authentication) {
        try {
            Long montantCentimes = Long.parseLong(
                    body.get("montant").toString());
            String description = body.getOrDefault(
                    "description", "Achat IRONFORGE").toString();

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(montantCentimes)
                            .setCurrency("eur")
                            .setDescription(description)
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams
                                            .AutomaticPaymentMethods.builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, String> result = new HashMap<>();
            result.put("clientSecret", intent.getClientSecret());
            result.put("intentId", intent.getId());

            return ResponseEntity.ok(
                    ApiResponse.succes("PaymentIntent créé", result));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // Confirmer paiement boutique
    @PostMapping("/confirmer-commande")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> confirmerCommande(
            @RequestBody Map<String, Object> body,
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

            String paymentIntentId = body.get("paymentIntentId")
                    .toString();
            Double total = Double.parseDouble(
                    body.get("total").toString());

            // Vérifier paiement Stripe
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

            if ("succeeded".equals(intent.getStatus())) {
                Commande commande = Commande.builder()
                        .membre(membre)
                        .total(total)
                        .statut(Commande.Statut.CONFIRMEE)
                        .paymentIntentId(paymentIntentId)
                        .build();

                commandeRepository.save(commande);

                // Ajouter points au membre
             int points = membre.getPoints() !=null?
                     membre.getPoints() : 0;
             membre.setPoints(
                        (int)(total * 0.1));
                membreRepository.save(membre);

                return ResponseEntity.ok(
                        ApiResponse.succes(
                                "Commande confirmée !", commande));
            } else {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur("Paiement non confirmé"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // Créer PaymentIntent pour abonnement
    @PostMapping("/create-subscription-intent")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> createSubscriptionIntent(
            @RequestBody Map<String, Object> body,
            Authentication authentication) {
        try {
            String planType = body.get("plan").toString();
            Double prix = Double.parseDouble(
                    body.get("prix").toString());

            long montantCentimes = (long)(prix * 100);

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(montantCentimes)
                            .setCurrency("eur")
                            .setDescription(
                                    "Abonnement IRONFORGE " + planType)
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams
                                            .AutomaticPaymentMethods.builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, String> result = new HashMap<>();
            result.put("clientSecret", intent.getClientSecret());
            result.put("intentId", intent.getId());
            result.put("plan", planType);

            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "PaymentIntent abonnement créé", result));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }

    // Confirmer abonnement après paiement
    @PostMapping("/confirmer-abonnement")
    @PreAuthorize("hasAnyRole('MEMBRE','COACH','ADMIN')")
    public ResponseEntity<ApiResponse> confirmerAbonnement(
            @RequestBody Map<String, Object> body,
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

            String paymentIntentId = body.get("paymentIntentId")
                    .toString();
            String planType = body.get("plan").toString();
            Double prix = Double.parseDouble(
                    body.get("prix").toString());
            int dureeJours = Integer.parseInt(
                    body.getOrDefault("dureeJours", "30").toString());

            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

            if ("succeeded".equals(intent.getStatus())) {
                java.time.LocalDate debut = java.time.LocalDate.now();
                java.time.LocalDate fin = debut.plusDays(dureeJours);

                Abonnement abonnement = Abonnement.builder()
                        .membre(membre)
                        .type(planType)
                        .prix(prix)
                        .dateDebut(debut)
                        .dateFin(fin)
                        .dureeJours(dureeJours)
                        .statut(Abonnement.Statut.ACTIF)
                        .paymentIntentId(paymentIntentId)
                        .build();

                abonnementRepository.save(abonnement);

                return ResponseEntity.ok(
                        ApiResponse.succes(
                                "Abonnement activé !", abonnement));
            } else {
                return ResponseEntity.badRequest().body(
                        ApiResponse.erreur("Paiement non confirmé"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage()));
        }
    }
}


