package br.com.fullstackedu.labpcp.controller.dto.request;

import jakarta.validation.constraints.AssertTrue;

import java.util.Objects;

public record MateriaUpdateRequest(
        String nome,
        Long id_curso
){
        @AssertTrue(message = "Ao menos uma das seguintes propriedades deve ser informada: [nome, id_curso]")
        public boolean isValidRequest() {
                return Objects.nonNull(nome)
                        || Objects.nonNull(id_curso);
        }
}