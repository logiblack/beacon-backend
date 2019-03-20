package com.lagranmoon.beacon.service;

import com.lagranmoon.beacon.model.AuthResponseDto;

/**
 * @author Lagranmoon
 */
public interface AuthService {

    AuthResponseDto auth(String code);

    Boolean verify(String token);
}