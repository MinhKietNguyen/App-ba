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
import org.example.batodolist.model.User;
import org.example.batodolist.repo.ProjectMemberRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.repo.UserRepository;
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
    private final UserRepository userRepository;
    private final GenericMapper genericMapper;

    @Autowired
    public TaskImplementService(TaskRepository taskRepository, ProjectRepository projectRepository,  ProjectMemberRepository projectMemberRepository, UserRepository userRepository, GenericMapper genericMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public TaskResponse getByID(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskResponse taskResponse = new TaskResponse();
        genericMapper.copy(task, taskResponse);
        
        // Check if this is a personal task
        if(task.getProject() != null){
            // Project task
            Project project = projectRepository.findById(task.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
            taskResponse.setProjectName(project.getName());
            if(task.getAssignedTo() != null){
                taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
            }
        } else {
            // Personal task
            taskResponse.setProjectName(null);
            if(task.getCreatedBy() != null){
                taskResponse.setAssignedTo(task.getCreatedBy().getUsername());
            }
        }
        
        return taskResponse;
    }

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = new Task();
        
        // Validate required fields
        if (taskRequest.getName() == null || taskRequest.getName().trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }
        if (taskRequest.getAssignedTo() == null || taskRequest.getAssignedTo().trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }
        
        // Check if this is a personal task (projectName is null or empty)
        boolean isPersonalTask = taskRequest.getProjectName() == null || taskRequest.getProjectName().trim().isEmpty();
        
        if (isPersonalTask) {
            // Personal task: find User by assignedTo (fullName) and set as createdBy
            String fullName = taskRequest.getAssignedTo().trim();
            // Try to find by fullName first, if not found, try username
            User user = userRepository.findUserByFullName(fullName)
                    .orElseGet(() -> userRepository.findUserByUsername(fullName)
                            .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND)));
            
            genericMapper.copy(taskRequest, task);
            task.setProject(null); // Personal tasks have no project
            task.setAssignedTo(null); // Personal tasks have no ProjectMember
            task.setCreatedBy(user); // Set the user as creator
            if (task.getProgress() == null) {
                task.setProgress(0); // Set default progress if not set
            }
            
            taskRepository.save(task);
            genericMapper.copy(task, taskResponse);
            taskResponse.setProjectName(null); // Personal tasks have no project name
            taskResponse.setAssignedTo(user.getUsername()); // Return username for personal tasks
        } else {
            // Project task: find Project and ProjectMember
            String projectName = taskRequest.getProjectName().trim();
            Project project = projectRepository.findProjectByName(projectName);
            if(project == null){
                throw new BadRequestException(ErrorCode.NOT_FOUND);
            }
            
            String username = taskRequest.getAssignedTo().trim();
            ProjectMember projectMember = projectMemberRepository.findProjectMemberByUser_Username(username);
            if(projectMember == null){
                throw new BadRequestException(ErrorCode.NOT_FOUND);
            }
            
            genericMapper.copy(taskRequest, task);
            task.setProject(project);
            task.setAssignedTo(projectMember);
            task.setCreatedBy(null); // Project tasks have no createdBy
            if (task.getProgress() == null) {
                task.setProgress(0); // Set default progress if not set
            }
            
            taskRepository.save(task);
            genericMapper.copy(task, taskResponse);
            taskResponse.setProjectName(project.getName());
            taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
        }
        
        return taskResponse;
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest, Long id) {
        TaskResponse taskResponse = new TaskResponse();
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        
        // Check if this is a personal task (projectName is null or empty)
        boolean isPersonalTask = taskUpdateRequest.getProjectName() == null || taskUpdateRequest.getProjectName().trim().isEmpty();
        
        if (isPersonalTask) {
            // Personal task: find User by assignedTo (fullName) and set as createdBy
            String fullName = taskUpdateRequest.getAssignedTo().trim();
            // Try to find by fullName first, if not found, try username
            User user = userRepository.findUserByFullName(fullName)
                    .orElseGet(() -> userRepository.findUserByUsername(fullName)
                            .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND)));
            
            genericMapper.copy(taskUpdateRequest, task);
            task.setProject(null); // Personal tasks have no project
            task.setAssignedTo(null); // Personal tasks have no ProjectMember
            task.setCreatedBy(user); // Set the user as creator
            
            taskRepository.save(task);
            genericMapper.copy(task, taskResponse);
            taskResponse.setProjectName(null); // Personal tasks have no project name
            taskResponse.setAssignedTo(user.getUsername()); // Return username for personal tasks
        } else {
            // Project task: find Project and ProjectMember
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
            task.setCreatedBy(null); // Project tasks have no createdBy
            
            taskRepository.save(task);
            genericMapper.copy(task, taskResponse);
            taskResponse.setProjectName(project.getName());
            taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
        }
        
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
                    
                    // Check if this is a personal task
                    if(x.getProject() != null){
                        // Project task
                        Project project = projectRepository.findById(x.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
                        taskResponse.setProjectName(project.getName());
                        if(x.getAssignedTo() != null){
                            taskResponse.setAssignedTo(x.getAssignedTo().getUser().getUsername());
                        }
                    } else {
                        // Personal task
                        taskResponse.setProjectName(null);
                        if(x.getCreatedBy() != null){
                            taskResponse.setAssignedTo(x.getCreatedBy().getUsername());
                        }
                    }
                    
                    return taskResponse;
                });
    }
}