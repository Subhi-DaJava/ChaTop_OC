package com.chatop.authservices.user_auth_service;

import com.chatop.dtos.*;
import com.chatop.exceptions.UserAlreadyExistsException;

public interface UserAuthService {

    Token register(SignUpRequest signUpRequest) throws UserAlreadyExistsException;

     Token login(AuthRequest authRequest);

    MeResponse retrieveProfile();
}
