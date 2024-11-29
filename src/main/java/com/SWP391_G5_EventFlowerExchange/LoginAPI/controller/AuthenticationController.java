package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.AuthenticationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.GoogleLoginRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.IntrospectRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.ApiResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.AuthenticationResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.GoogleLoginResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.IntrospectResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result= authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().code(1000)
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result= authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().code(1000)
                .result(result)
                .build();
    }

    // Endpoint to handle Google login
    @PostMapping("/google/login")
    ApiResponse<GoogleLoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        var result = authenticationService.authenticateWithGoogle(request); // Handle Google token here
        return ApiResponse.<GoogleLoginResponse>builder().code(1000)
                .result(result)
                .build();
    }
}
