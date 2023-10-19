package com.chatop.dtos;

import java.time.LocalDate;

public record AuthResponse(Integer id, String name, String email, LocalDate created_at, LocalDate updated_at) { }
