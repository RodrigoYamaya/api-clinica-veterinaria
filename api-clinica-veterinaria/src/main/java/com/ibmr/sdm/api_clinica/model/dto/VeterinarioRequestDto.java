package com.ibmr.sdm.api_clinica.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VeterinarioRequestDto(
        @NotBlank(message = "O nome completo é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(min = 11, max = 11, message = "O CPF deve ter exatos 11 caracteres")
        String cpf,

        @NotBlank(message = "O número do CRMV é obrigatório")
        String crmvNumero,

        @NotBlank(message = "A UF do CRMV é obrigatória")
        @Size(min = 2, max = 2, message = "A UF do CRMV deve ter 2 caracteres")
        String crmvUf,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,

        String telefone,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha
) {
}
