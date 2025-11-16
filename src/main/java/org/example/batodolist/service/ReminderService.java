package org.example.batodolist.service;

import org.example.batodolist.dto.request.ReminderRequest;
import org.example.batodolist.dto.request.ReminderUpdateRequest;
import org.example.batodolist.dto.response.ReminderResponse;
import org.springframework.data.domain.Page;

public interface ReminderService {
    ReminderResponse getById(Long id);
    ReminderResponse create(ReminderRequest reminderRequest);
    ReminderResponse update(ReminderUpdateRequest reminderUpdateRequest, Long id);
    void delete (Long id);
    Page<ReminderResponse> paging(int offset, int limit);
}
