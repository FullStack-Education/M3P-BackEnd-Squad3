package br.com.fullstackedu.labpcp.controller;

import br.com.fullstackedu.labpcp.controller.dto.response.AlunoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.CursoResponse;
import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import br.com.fullstackedu.labpcp.service.DashboardService;
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
public class DashboardController {
    private final DashboardService dashboardService;

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
