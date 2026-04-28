
package com.ironforge.controller;

import com.ironforge.dto.ApiResponse;
import com.ironforge.dto.LoginRequest;
import com.ironforge.dto.LoginResponse;
import com.ironforge.dto.RegisterRequest;
import com.ironforge.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response =
                    authService.login(request);
            return ResponseEntity.ok(
                    ApiResponse.succes(
                            "Connexion réussie", response)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(
                            "Email ou mot de passe incorrect")
            );
        }
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        try {
            String message =
                    authService.register(request);
            return ResponseEntity.ok(
                    ApiResponse.succes(message)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.erreur(e.getMessage())
            );
        }
    }

    // GET /api/auth/test
    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(
                ApiResponse.succes(
                        "IRONFORGE API fonctionne !")
        );
    }
}