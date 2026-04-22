package com.fatec.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
public class EmailVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Geração automática do ID (auto increment)
    private Long id;  // Ajuste o tipo para Long

    private String token;
    private boolean status;

    @Column(name = "user_email")  // Definindo o nome da coluna no banco de dados
    private String userEmail;

    private LocalDateTime exp;

    // Construtor
    public EmailVerify(String token, boolean status, String userEmail, LocalDateTime exp) {
        this.token = token;
        this.status = status;
        this.userEmail = userEmail;
        this.exp = exp;
    }

    public EmailVerify() {
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getExp() {
        return exp;
    }

    public void setExp(LocalDateTime exp) {
        this.exp = exp;
    }

    public boolean isExpiradoOuUsado(){
        return this.getExp().isBefore(LocalDateTime.now()) || this.isStatus();
    }
}