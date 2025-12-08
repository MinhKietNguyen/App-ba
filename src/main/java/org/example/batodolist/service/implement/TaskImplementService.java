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

import java.time.LocalDateTime;

@Service
public class TaskImplementService implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final GenericMapper genericMapper;

    @Autowired
    public TaskImplementService(TaskRepository taskRepository, ProjectRepository projectRepository,
                                ProjectMemberRepository projectMemberRepository, UserRepository userRepository,
                                GenericMapper genericMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public TaskResponse getByID(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        return mapToTaskResponse(task);
    }

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        // Validate required fields
        validateTaskRequest(taskRequest);

        Task task = new Task();
        boolean isPersonalTask = isPersonalTaskRequest(taskRequest);

        if (isPersonalTask) {
            createPersonalTask(task, taskRequest);
        } else {
            createProjectTask(task, taskRequest);
        }

        taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        // Validate update request
        validateTaskUpdateRequest(taskUpdateRequest);

        if (task.isPersonalTask()) {
            updatePersonalTask(task, taskUpdateRequest);
        } else if (task.isProjectTask()) {
            updateProjectTask(task, taskUpdateRequest);
        } else {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }

        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        taskRepository.delete(task);
    }

    @Override
    public Page<TaskResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return taskRepository.findAll(pageable).map(this::mapToTaskResponse);
    }

    // ========== HELPER METHODS ==========

    private void validateTaskRequest(TaskRequest taskRequest) {
        if (taskRequest.getName() == null || taskRequest.getName().trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }
        if (taskRequest.getAssignedTo() == null || taskRequest.getAssignedTo().trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }
        // Validate date if provided
        if (taskRequest.getDueDate() != null && taskRequest.getDueDate().isBefore(java.time.LocalDate.now())) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateTaskUpdateRequest(TaskUpdateRequest taskUpdateRequest) {
        // Validate progress if provided
        if (taskUpdateRequest.getProgress() != null) {
            if (taskUpdateRequest.getProgress() < 0 || taskUpdateRequest.getProgress() > 100) {
                throw new BadRequestException(ErrorCode.INVALID_INPUT);
            }
        }
    }

    private boolean isPersonalTaskRequest(TaskRequest taskRequest) {
        return taskRequest.getProjectName() == null || taskRequest.getProjectName().trim().isEmpty();
    }

    private void createPersonalTask(Task task, TaskRequest taskRequest) {
        String username = taskRequest.getAssignedTo().trim();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        genericMapper.copy(taskRequest, task);
        task.setProject(null);
        task.setAssignedTo(null);
        task.setCreatedBy(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setProgress(0);

        // Map date field to dueDate
        if (taskRequest.getDueDate() != null) {
            task.setDueDate(taskRequest.getDueDate());
        }
    }

    private void createProjectTask(Task task, TaskRequest taskRequest) {
        String projectName = taskRequest.getProjectName().trim();
        Project project = projectRepository.findProjectByName(projectName);
        if (project == null) {
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }

        String username = taskRequest.getAssignedTo().trim();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        ProjectMember projectMember = projectMemberRepository
                .findProjectMemberByUserAndProject(user, project)
                .orElseThrow(() -> new BadRequestException(ErrorCode.THIS_PERSON_DOES_NOT_BELONG_TO_THE_PROJECT));

        genericMapper.copy(taskRequest, task);
        task.setProject(project);
        task.setAssignedTo(projectMember);
        task.setCreatedAt(LocalDateTime.now());
        task.setProgress(0);

        // Map date field to dueDate
        if (taskRequest.getDueDate() != null) {
            task.setDueDate(taskRequest.getDueDate());
        }
    }

    private void updatePersonalTask(Task task, TaskUpdateRequest taskUpdateRequest) {
        // For personal tasks, we can only update basic fields
        // Cannot change assignedTo (which is actually createdBy for personal tasks)
        if (taskUpdateRequest.getName() != null && !taskUpdateRequest.getName().trim().isEmpty()) {
            task.setName(taskUpdateRequest.getName());
        }
        if (taskUpdateRequest.getDescription() != null) {
            task.setDescription(taskUpdateRequest.getDescription());
        }
        if (taskUpdateRequest.getStatus() != null) {
            task.setStatus(taskUpdateRequest.getStatus());
        }
        if (taskUpdateRequest.getPriority() != null) {
            task.setPriority(taskUpdateRequest.getPriority());
        }
        if (taskUpdateRequest.getProgress() != null) {
            task.setProgress(taskUpdateRequest.getProgress());
        }
    }

    private void updateProjectTask(Task task, TaskUpdateRequest taskUpdateRequest) {
        // Update assignedTo if provided
        if (taskUpdateRequest.getAssignedTo() != null && !taskUpdateRequest.getAssignedTo().trim().isEmpty()) {
            String username = taskUpdateRequest.getAssignedTo().trim();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

            ProjectMember projectMember = projectMemberRepository
                    .findProjectMemberByUserAndProject(user, task.getProject())
                    .orElseThrow(() -> new BadRequestException(ErrorCode.THIS_PERSON_DOES_NOT_BELONG_TO_THE_PROJECT));

            task.setAssignedTo(projectMember);
        }

        // Update other fields
        if (taskUpdateRequest.getName() != null && !taskUpdateRequest.getName().trim().isEmpty()) {
            task.setName(taskUpdateRequest.getName());
        }
        if (taskUpdateRequest.getDescription() != null) {
            task.setDescription(taskUpdateRequest.getDescription());
        }
        if (taskUpdateRequest.getStatus() != null) {
            task.setStatus(taskUpdateRequest.getStatus());
        }
        if (taskUpdateRequest.getPriority() != null) {
            task.setPriority(taskUpdateRequest.getPriority());
        }
        if (taskUpdateRequest.getProgress() != null) {
            task.setProgress(taskUpdateRequest.getProgress());
        }
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        genericMapper.copy(task, taskResponse);

        if (task.isProjectTask()) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
            taskResponse.setProjectName(project.getName());
            if (task.getAssignedTo() != null) {
                taskResponse.setAssignedTo(task.getAssignedTo().getUser().getUsername());
            }
        } else if (task.isPersonalTask()) {
            taskResponse.setProjectName(null);
            if (task.getCreatedBy() != null) {
                taskResponse.setAssignedTo(task.getCreatedBy().getUsername());
            }
        }

        return taskResponse;
    }
}