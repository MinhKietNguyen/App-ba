package org.example.batodolist.service;

import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.request.MessageUpdateRequest;
import org.example.batodolist.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface MessageService {
    MessageResponse getById(Long id);
    MessageResponse create(MessageRequest messageRequest);
    MessageResponse update(MessageUpdateRequest messageUpdateRequest, Long id);
    void delete (Long id);
    Page<MessageResponse> paging(int offset, int limit);
}
