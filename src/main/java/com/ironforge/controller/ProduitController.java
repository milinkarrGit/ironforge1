package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.model.Produit;
import com.ironforge.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitRepository produitRepository;

    // GET /api/produits — PUBLIC
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProduits() {
        try {
            List<Produit> produits =
                    produitRepository.findAll();
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Produits récupérés", produits)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/produits/{id} — PUBLIC
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProduitById(
            @PathVariable Long id) {
        try {
            Produit produit = produitRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Produit non trouvé")
                    );
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Produit récupéré", produit)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/produits/categorie/{id}
    @GetMapping("/categorie/{id}")
    public ResponseEntity<ApiResponse> getProduitsByCategorie(
            @PathVariable Long id) {
        try {
            List<Produit> produits =
                    produitRepository.findByCategorieId(id);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Produits par catégorie", produits)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // POST /api/produits — ADMIN seulement
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduit(
            @RequestBody Produit produit) {
        try {
            Produit saved = produitRepository.save(produit);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Produit créé", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // PUT /api/produits/{id} — ADMIN seulement
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduit(
            @PathVariable Long id,
            @RequestBody Produit produitUpdate) {
        try {
            Produit produit = produitRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Produit non trouvé")
                    );

            produit.setNom(produitUpdate.getNom());
            produit.setDescription(
                    produitUpdate.getDescription());
            produit.setPrix(produitUpdate.getPrix());
            produit.setStock(produitUpdate.getStock());
            produit.setPromotion(
                    produitUpdate.getPromotion());

            Produit saved = produitRepository.save(produit);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Produit mis à jour", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // DELETE /api/produits/{id} — ADMIN seulement
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduit(
            @PathVariable Long id) {
        try {
            produitRepository.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.succes("Produit supprimé")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }
}