package com.ironforge.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Utilisateur {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false,length =50 )
    private String nom;

    @NotBlank(message = "Le prenom est obligatoire")
    @Column(name = "prenom", nullable = false,length =50 )
    private String prenom;

    @NotBlank(message = " email est obligatoire")
    @Email(message = "Email invalide")
    @Column(name = "email", nullable = false,unique = true, length =100 )
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(name = "mot_de_passe", nullable = false )
    private String motDePasse;


    @Column(name = "telephone", length =50 )
    private String telephone;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false )
    private Role role;

    @Column(name = "actif" )
    private Boolean actif = true;

    @PrePersist
    public void prePersist(){
        dateInscription = LocalDateTime.now();
    }

    public enum Role{
        ADMIN , COACH , MEMBRE , VISITEUR
    }
}
