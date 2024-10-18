package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.response.AlunoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {
    private final LoginService loginService;
    private final AlunoService alunoService;
    private final TurmaService turmaService;
    private final DocenteService docenteService;

    private static final List<String> commonPermissions = List.of("ALUNO");

    private boolean _isAuthorized(String actualToken, List<String> unauthorizedPerfis) {
        String papelName =  loginService.getFieldInToken(actualToken, "scope");
        return !unauthorizedPerfis.contains(papelName);
    }

    private boolean _isAuthorized(String actualToken) {
        return _isAuthorized(actualToken, commonPermissions);
    }

    public DashboardResponse getCounts(String actualToken) {
        try {
            if (!_isAuthorized(actualToken)){
                String errMessage = "O Usuário logado não tem acesso a essa funcionalidade";
                log.error(errMessage);
                return new DashboardResponse(false, LocalDateTime.now(), errMessage, null, null, null, HttpStatus.UNAUTHORIZED);
            }
            long alunosCount = alunoService.count();
            long turmasCount = turmaService.count();
            long docentesCount = docenteService.count();

            return new DashboardResponse(true, LocalDateTime.now(), "Contagem de entidades encontradas", alunosCount, turmasCount, docentesCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Falha ao buscar quantidade de entidades cadastradas. Erro: {}", e.getMessage());
            return new DashboardResponse(false, LocalDateTime.now(), e.getMessage(), null, null, null, HttpStatus.BAD_REQUEST);
        }
    }
}
