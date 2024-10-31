package br.com.fullstackedu.labpcp.database.repository;

import br.com.fullstackedu.labpcp.database.entity.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurmaRepository extends JpaRepository<TurmaEntity, Long> {
    Optional<TurmaEntity> findByNome(String nome);
}
