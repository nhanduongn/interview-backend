package com.nhandn.backend.service;

import com.nhandn.backend.dto.AuthenticationRequest;
import com.nhandn.backend.dto.AuthenticationResponse;
import com.nhandn.backend.dto.UserRequest;
import com.nhandn.backend.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request) throws ResourceNotFoundException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
