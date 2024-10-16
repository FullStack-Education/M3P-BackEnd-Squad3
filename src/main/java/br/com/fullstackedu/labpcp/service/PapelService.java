package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.database.entity.PapelEntity;
import br.com.fullstackedu.labpcp.database.repository.PapelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PapelService {
    private final PapelRepository papelRepository;
    public PapelService(PapelRepository papelRepository) {
        this.papelRepository = papelRepository;
    }

    public PapelEntity getPapelById(Long id) {
        log.info("Buscando Papel por id [{}]",id);
        return papelRepository.findById(id).orElse(null);
    }
}
