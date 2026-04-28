
package com.ironforge.dto;

import com.ironforge.model.Commande;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse {

    private Boolean succes;
    private String message;
    private Object data;



    // Constructeur succès avec données
    public static ApiResponse succes(
            String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    // Constructeur succès sans données
    public static ApiResponse succes(String message) {
        return new ApiResponse(true, message, null);
    }

    // Constructeur erreur
    public static ApiResponse erreur(String message) {
        return new ApiResponse(false, message, null);
    }

}