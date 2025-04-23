package com.fatec.model;

import java.time.LocalDateTime;

import com.fatec.dto.EmailRecordDto;
import jakarta.persistence.*;  // A importação do JPA (Jakarta Persistence API)

@Entity
public class EmailVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long emailId;

    private Long usuarioId;
    private String emailFrom;
    private String emailTo;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime sendDateEmail;
    private StatusEmail statusEmail;

    // Método para converter um EmailRecordDto em EmailVerify
    public static EmailVerify fromDto(EmailRecordDto dto, Long usuarioId, String emailFrom) {
        EmailVerify emailVerify = new EmailVerify();
        emailVerify.setEmailTo(dto.to());
        emailVerify.setSubject(dto.subject());
        emailVerify.setText(dto.body());
        emailVerify.setUsuarioId(usuarioId);
        emailVerify.setEmailFrom(emailFrom);
        emailVerify.setSendDateEmail(LocalDateTime.now());  // Data e hora do envio
        emailVerify.setStatusEmail(StatusEmail.PENDING);  // Status inicial (pode ser alterado depois)
        return emailVerify;
    }

    // Getters e Setters omitidos para brevidade...

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendDateEmail() {
        return sendDateEmail;
    }

    public void setSendDateEmail(LocalDateTime sendDateEmail) {
        this.sendDateEmail = sendDateEmail;
    }

    public StatusEmail getStatusEmail() {
        return statusEmail;
    }

    public void setStatusEmail(StatusEmail statusEmail) {
        this.statusEmail = statusEmail;
    }
}
