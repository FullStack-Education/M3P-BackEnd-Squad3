package br.com.fullstackedu.labpcp.database.repository;

import br.com.fullstackedu.labpcp.database.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PapelRepository extends JpaRepository <PapelEntity, Long> {
    Optional<PapelEntity> findByNome(String nome);
}
