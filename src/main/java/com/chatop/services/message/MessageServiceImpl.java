package com.chatop.services.message;

import com.chatop.dtos.MessageDTO;
import com.chatop.exceptions.RentalNotFondException;
import com.chatop.models.Message;
import com.chatop.models.Rental;
import com.chatop.models.User;
import com.chatop.repositories.MessageRepository;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    @Override
    public void sendMessage(MessageDTO messageDTO) {

        Message message = Message.builder()
                .message(messageDTO.message())
                .rental(getRentalById(messageDTO.rental_id()))
                .user(getUserById(messageDTO.user_id()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        messageRepository.save(message);

        log.info("Message send with success");
    }


    private Rental getRentalById(Integer id) {
        if(id == null) {
            log.error("Rental not found");
            throw new IllegalArgumentException("Rental id should not be null");
        }
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new RentalNotFondException("Rental not found"));
        log.info("Rental found with success");
        return rental;
    }
    private User getUserById(Integer id) {
        if(id == null) {
            log.error("User not found");
            throw new IllegalArgumentException("User id should not be null");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("User found with success");
        return user;
    }

}
