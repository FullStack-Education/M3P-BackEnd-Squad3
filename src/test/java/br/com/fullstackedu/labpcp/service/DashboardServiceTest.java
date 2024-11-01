package br.com.fullstackedu.labpcp.service;

import br.com.fullstackedu.labpcp.controller.dto.response.DashboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {
    @Mock
    private LoginService loginService;

    @Mock
    private AlunoService alunoService;

    @Mock
    private TurmaService turmaService;

    @Mock
    private DocenteService docenteService;

    @InjectMocks
    private DashboardService dashboardService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCounts_UnauthorizedAcess() {
        String token = "invalidToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ALUNO");

        DashboardResponse response = dashboardService.getCounts(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.httpStatus());
        assertEquals("O Usuário logado não tem acesso a essa funcionalidade", response.message());
        verify(loginService).getFieldInToken(token, "scope");
        verifyNoInteractions(alunoService, turmaService, docenteService);
    }

    @Test
    void testGetCounts_AuthorizedAcess(){
        String token = "validToken";
        when(loginService.getFieldInToken(token, "scope")).thenReturn("ADM");
        when(alunoService.count()).thenReturn(30L);
        when(turmaService.count()).thenReturn(20L);
        when(docenteService.count()).thenReturn(10L);


        DashboardResponse response = dashboardService.getCounts(token);


        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Contagem de entidades encontradas", response.message());
        assertEquals(30L, response.alunosCount());
        assertEquals(20L, response.turmasCount());
        assertEquals(10L, response.docentesCount());
        verify(loginService).getFieldInToken(token, "scope");
        verify(alunoService).count();
        verify(turmaService).count();
        verify(docenteService).count();
    }

    @Test
    void testGetCounts_ServiceThrowsException() {
        String token = "validToken";
        when(loginService.getFieldInToken(anyString(), eq("scope"))).thenReturn("ADM");
        when(alunoService.count()).thenThrow(new RuntimeException("Database error"));

        DashboardResponse response = dashboardService.getCounts(token);

        assertEquals(HttpStatus.BAD_REQUEST, response.httpStatus());
        assertEquals("Database error", response.message());
    }

}


