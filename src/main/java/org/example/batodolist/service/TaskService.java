package org.example.batodolist.service;

import java.util.List;

import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;

public interface TaskService {
    TaskResponse create(TaskRequest request);
    TaskResponse update(Long id, TaskUpdateRequest request);
    TaskResponse getById(Long id);
    List<TaskResponse> getAll();
    void delete(Long id);
}