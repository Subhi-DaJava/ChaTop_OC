package com.chatop.services.message;

import com.chatop.dtos.MessageDTO;

public interface MessageService {
    void sendMessage(MessageDTO messageDTO);
}
