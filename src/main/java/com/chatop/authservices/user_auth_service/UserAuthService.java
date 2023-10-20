package com.chatop.authservices.user_auth_service;

import com.chatop.dtos.*;
import com.chatop.exceptions.UserAlreadyExistsException;

public interface UserAuthService {

    Token register(UserDTO userDTO) throws UserAlreadyExistsException;

     AuthResponseDTO<?> login(AuthRequest authRequest);

    AuthResponse retrieveProfile();
}
