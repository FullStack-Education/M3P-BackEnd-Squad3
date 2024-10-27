package br.com.fullstackedu.labpcp.database.repository;

import br.com.fullstackedu.labpcp.database.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MateriaRepository extends JpaRepository<MateriaEntity, Long> {
    List<MateriaEntity> findByCursoId(Long cursoId);

    Optional<MateriaEntity> findByNome(String nome);


}
