package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.response.AlunoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.CursoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import br.com.fullstackedu.labpcp.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard - GET", description = "Retorna o Dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    @Operation(summary = "Buscar dados para o dashboard")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados encontrados",
                    useReturnTypeSchema = true
            ),
    })
    @GetMapping
    public ResponseEntity<DashboardResponse> getEntitiesAmount(@RequestHeader(name = "Authorization") String authToken) {
        log.info("GET /dashboard");
        String actualToken = authToken.substring(7);
        DashboardResponse response = dashboardService.getCounts(actualToken);
        if (response.success()) {
            log.info("GET /dashboard -> OK ");
        } else {
            log.error("GET /dashboard -> {} ", response.httpStatus());
        }
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
