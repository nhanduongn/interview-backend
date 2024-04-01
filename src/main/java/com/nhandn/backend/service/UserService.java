package com.nhandn.backend.service;

import com.nhandn.backend.dto.UserRequest;
import com.nhandn.backend.dto.UserResponse;
import com.nhandn.backend.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface UserService {


    UserResponse create(UserRequest request) throws Exception;
    UserResponse getUser(Integer id);
    Page<UserResponse> getAllUser(Integer page, Integer size);
    void updateUser(UserRequest request);
    void deleteUser(String email) throws Exception;
}
