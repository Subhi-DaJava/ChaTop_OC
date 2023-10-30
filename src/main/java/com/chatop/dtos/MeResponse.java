package com.chatop.dtos;

import java.time.LocalDateTime;

public record MeResponse(Integer id, String name, String email, LocalDateTime created_at, LocalDateTime updated_at) { }
