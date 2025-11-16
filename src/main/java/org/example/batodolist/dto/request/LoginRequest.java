package org.example.batodolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(example = "Tester")
    private String username;

    @Schema(example = "111")
    private String password;
}
