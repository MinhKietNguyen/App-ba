package org.example.batodolist.service;

import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.response.MessageResponse;

public interface WebSocketService {

    MessageResponse sendMessage(Long projectId, MessageRequest messageRequest);

    void sendReply(Long projectId, MessageRequest messageRequest);
}
