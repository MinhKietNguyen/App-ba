package org.example.batodolist.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.response.MessageResponse;
import org.example.batodolist.service.MessageService;
import org.example.batodolist.service.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketImplementService implements WebSocketService{
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public MessageResponse sendMessage(Long projectId, MessageRequest messageRequest) {
        log.info("Sending message to project {}", projectId);

        // Lưu message vào database
        MessageResponse response = messageService.create(messageRequest);

        log.info("Message created with ID: {}", response.getId());
        return response;
    }

    @Override
    @Transactional
    public void sendReply(Long projectId, MessageRequest messageRequest) {
        log.info("Sending reply to project {}", projectId);

        // Lưu reply vào database
        MessageResponse response = messageService.create(messageRequest);

        messagingTemplate.convertAndSend(
                "/topic/project/" + projectId,
                response
        );

        if (messageRequest.getParentId() != null) {
            MessageResponse parentMessage = messageService.getById(messageRequest.getParentId());
            Long parentUserId = parentMessage.getParentId();

            messagingTemplate.convertAndSend(
                    "/queue/reply/" + parentUserId,
                    response
            );

            log.info("Reply notification sent to user {}", parentUserId);
        }
    }

}
