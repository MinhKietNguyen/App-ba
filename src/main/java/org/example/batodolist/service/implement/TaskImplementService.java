package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.Task;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskImplementService implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskImplementService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskResponse getByID(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(task, taskResponse);
        if(task.getProject().getId() != null){
            Project project = projectRepository.findById(task.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
            taskResponse.setProjectName(project.getName());
        }
        return taskResponse;
    }

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = new Task();
        Project project = taskRequest.getProjectName() == null ? null : projectRepository.findProjectByName(taskRequest.getProjectName());
        BeanUtils.copyProperties(taskRequest, task);
        task.setProject(project);
        taskRepository.save(task);
        BeanUtils.copyProperties(task, taskResponse);
        return taskResponse;
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest, Long id) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        Project project = taskUpdateRequest.getProjectName() == null ? null : projectRepository.findProjectByName(taskUpdateRequest.getProjectName());
        BeanUtils.copyProperties(taskUpdateRequest, task);
        task.setProject(project);
        taskRepository.save(task);
        BeanUtils.copyProperties(task, taskResponse);
        return taskResponse;
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        taskRepository.delete(task);
    }
    @Override
    public Page<TaskResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return taskRepository.findAll(pageable).map(
                x -> {
                    TaskResponse taskResponse = new TaskResponse();
                    BeanUtils.copyProperties(x, taskResponse);
                    if(x.getProject().getId() != null){
                        Project project = projectRepository.findById(x.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
                        taskResponse.setProjectName(project.getName());
                    }
                    return taskResponse;
                });
    }
}