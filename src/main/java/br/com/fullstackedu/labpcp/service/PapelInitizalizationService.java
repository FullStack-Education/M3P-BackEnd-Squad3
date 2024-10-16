package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.PapelEntity;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class PapelInitizalizationService {
    private final PapelRepository papelRepository;

    public PapelInitizalizationService(PapelRepository papelRepository) {
        this.papelRepository = papelRepository;
    }

    private void insertIfNotExists(Long id, String nome) {
        if (papelRepository.findByNome(nome).isEmpty()) {
            PapelEntity newPapel = new PapelEntity();
            newPapel.setId(id);
            newPapel.setNome(nome);
            papelRepository.save(newPapel);
        }
    }

    @PostConstruct
    public void initPapeis() {
        insertIfNotExists(1L, "ADM");
        insertIfNotExists(2L, "PEDAGOGICO");
        insertIfNotExists(3L, "RECRUITER");
        insertIfNotExists(4L, "PROFESSOR");
        insertIfNotExists(5L, "ALUNO");
    }
}
