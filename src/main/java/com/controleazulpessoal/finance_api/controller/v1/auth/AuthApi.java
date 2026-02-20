package com.controleazulpessoal.finance_api.controller.v1.auth;

import com.controleazulpessoal.finance_api.controller.v1.auth.request.LoginRequest;
import com.controleazulpessoal.finance_api.usecase.auth.output.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthApi {

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);
}