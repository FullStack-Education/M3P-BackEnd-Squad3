package br.com.fullstackedu.labpcp.controller;


import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import br.com.fullstackedu.labpcp.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEntitiesAmount_AuthorizedAcess() {

        String token = "validToken";
        DashboardResponse response = new DashboardResponse(
                true,
                LocalDateTime.now(),
                "Dados encontrados",
                30L,
                20L,
                10L,
                HttpStatus.OK
        );

        when(dashboardService.getCounts(anyString())).thenReturn(response);


        ResponseEntity<DashboardResponse> result = dashboardController.getEntitiesAmount(token);


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Dados encontrados", result.getBody().message());
        assertEquals(30L, result.getBody().alunosCount());
        assertEquals(20L, result.getBody().turmasCount());
        assertEquals(10L, result.getBody().docentesCount());
        verify(dashboardService).getCounts(anyString());
    }

    @Test
    void testGetEntitiesAmount_UnauthorizedAccess() {
        String token = "invalidToken";
        DashboardResponse response = new DashboardResponse(
                false,
                LocalDateTime.now(),
                "O Usuário logado não tem acesso a essa funcionalidade",
                null,
                null,
                null,
                HttpStatus.UNAUTHORIZED
        );

        when(dashboardService.getCounts(anyString())).thenReturn(response);


        ResponseEntity<DashboardResponse> result = dashboardController.getEntitiesAmount(token);


        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", result.getBody().message());
        verify(dashboardService).getCounts(anyString());
    }

    @Test
    void testGetEntitiesAmount_BadRequest() {
        String token = "validToken";
        DashboardResponse response = new DashboardResponse(
                false,
                LocalDateTime.now(),
                "Falha ao buscar quantidade de entidades cadastradas",
                null,
                null,
                null,
                HttpStatus.BAD_REQUEST
        );

        when(dashboardService.getCounts(anyString())).thenReturn(response);

        ResponseEntity<DashboardResponse> result = dashboardController.getEntitiesAmount(token);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Falha ao buscar quantidade de entidades cadastradas", result.getBody().message());

        verify(dashboardService).getCounts(anyString());
    }
}
