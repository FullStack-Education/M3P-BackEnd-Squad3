package br.com.fullstackedu.labpcp.controller.dto.response;

import br.com.fullstackedu.labpcp.database.entity.AlunoEntity;

public record AlunoScoreDTO(AlunoEntity alunoEntity, double pontuacao) {
}
