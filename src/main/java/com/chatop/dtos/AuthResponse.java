package com.chatop.dtos;

import java.time.LocalDateTime;

public record AuthResponse(Integer id, String name, String email, LocalDateTime created_at, LocalDateTime updated_at) { }
