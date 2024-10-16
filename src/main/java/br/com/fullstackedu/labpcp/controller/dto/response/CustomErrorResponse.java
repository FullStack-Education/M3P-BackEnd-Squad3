package br.com.fullstackedu.labpcp.controller.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class CustomErrorResponse {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String trace;
}