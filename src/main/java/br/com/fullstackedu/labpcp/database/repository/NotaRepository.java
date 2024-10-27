package br.com.fullstackedu.labpcp.database.repository;

import br.com.fullstackedu.labpcp.database.entity.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<NotaEntity, Long> {
    List<NotaEntity> findByAlunoId(Long alunoId);

    Optional<NotaEntity> findByIdAndAlunoId(Long notaId, Long AlunoId);
    
    int countByAlunoId(Long idAluno);
    
}
