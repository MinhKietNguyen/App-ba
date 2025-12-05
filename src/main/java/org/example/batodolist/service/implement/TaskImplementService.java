package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.model.Task;
import org.example.batodolist.repo.ProjectMemberRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskImplementService implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final GenericMapper genericMapper;

    @Autowired
    public TaskImplementService(TaskRepository taskRepository, ProjectRepository projectRepository,  ProjectMemberRepository projectMemberRepository, GenericMapper genericMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public TaskResponse getByID(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskResponse taskResponse = new TaskResponse();
        genericMapper.copy(task, taskResponse);
        if(task.getProject().getId() != null){
            Project project = projectRepository.findById(task.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
            taskResponse.setProjectName(project.getName());
        }
        taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
        return taskResponse;
    }

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = new Task();
        Project project = projectRepository.findProjectByName(taskRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        ProjectMember projectMember = projectMemberRepository.findProjectMemberByUser_Username(taskRequest.getAssignedTo());
        if(projectMember == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        genericMapper.copy(taskRequest, task);
        task.setProject(project);
        task.setAssignedTo(projectMember);
        taskRepository.save(task);
        genericMapper.copy(task, taskResponse);
        taskResponse.setProjectName(project.getName());
        taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
        return taskResponse;
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest, Long id) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        Project project = projectRepository.findProjectByName(taskUpdateRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        ProjectMember projectMember = projectMemberRepository.findProjectMemberByUser_Username(taskUpdateRequest.getAssignedTo());
        if(projectMember == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        genericMapper.copy(taskUpdateRequest, task);
        task.setProject(project);
        task.setAssignedTo(projectMember);
        taskRepository.save(task);
        genericMapper.copy(task, taskResponse);
        taskResponse.setProjectName(project.getName());
        taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
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
                    genericMapper.copy(x, taskResponse);
                    if(x.getProject().getId() != null){
                        Project project = projectRepository.findById(x.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
                        taskResponse.setProjectName(project.getName());
                    }
                    taskResponse.setAssignedTo(x.getAssignedTo().getUser().getUsername());
                    return taskResponse;
                });
    }
}