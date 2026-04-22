package com.fatec.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailRecordDTO(
        @NotBlank(message = "O campo 'to' é obrigatório.") String to,
        @NotBlank(message = "O campo 'subject' é obrigatório.") String subject,
        @NotBlank(message = "O campo 'body' é obrigatório.") String body
) {}