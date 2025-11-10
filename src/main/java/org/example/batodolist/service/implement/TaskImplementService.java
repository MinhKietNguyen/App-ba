package org.example.batodolist.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;
import org.example.batodolist.model.Task;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskImplementService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Override
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Task not found: " + id));

        if (request.getName() != null) {
            task.setName(request.getName());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getProgress() != null) {
            task.setProgress(request.getProgress()); // Task.java đã có validate 0..100
        }
        
        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Task not found: " + id));
        return toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new BadRequestException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

   
    private TaskResponse toResponse(Task task) {
        return new TaskResponse();
    }
}