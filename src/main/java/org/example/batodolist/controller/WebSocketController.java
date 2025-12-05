package org.example.batodolist.controller;

import lombok.RequiredArgsConstructor;
import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.response.MessageResponse;
import org.example.batodolist.service.WebSocketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService webSocketService;

    @MessageMapping("/chat/{projectId}")
    @SendTo("/topic/project/{projectId}")
    public MessageResponse sendMessage(@DestinationVariable Long projectId,
                                       @Payload MessageRequest messageRequest,
                                       SimpMessageHeaderAccessor headerAccessor) {
        return webSocketService.sendMessage(projectId, messageRequest);
    }

    @MessageMapping("/chat/{projectId}/reply")
    public void sendReply(@DestinationVariable Long projectId,
                          @Payload MessageRequest messageRequest) {
        webSocketService.sendReply(projectId, messageRequest);
    }
}
