package br.com.fullstackedu.labpcp.database.repository;

import br.com.fullstackedu.labpcp.database.entity.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {
    Optional<DocenteEntity> findByNome(String nome);
}
