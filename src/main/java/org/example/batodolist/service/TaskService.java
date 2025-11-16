package org.example.batodolist.service;

import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;
import org.springframework.data.domain.Page;

public interface TaskService {
    TaskResponse getByID(Long id);
    TaskResponse create(TaskRequest taskRequest);
    TaskResponse update(TaskUpdateRequest taskUpdateRequest, Long id);
    void delete (Long id);

    Page<TaskResponse> paging(int offset, int limit);
}