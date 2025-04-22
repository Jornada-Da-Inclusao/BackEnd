package com.fatec.dto;

public record EmailRecordDto (
        Long id,
        Long usuarioId,
        String emailFrom,
        String emailTo,
        String subject,
        String text,
        String sendDateEmail,
        String statusEmail
) {}
