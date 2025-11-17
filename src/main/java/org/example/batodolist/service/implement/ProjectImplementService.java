package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.UserDTO;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.request.ProjectUpdateRequest;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.ProjectMemberRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.UserRepository;
import org.example.batodolist.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectImplementService implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Autowired
    public ProjectImplementService(ProjectRepository projectRepository, UserRepository userRepository, ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    public ProjectResponse getById(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);

        User user = project.getManager();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        ProjectMember projectMember = projectMemberRepository.findProjectMemberByProjectId(id);
        userDTO.setRoleInProject(projectMember.getRoleInProject());

        projectResponse.setManager(userDTO);
        return projectResponse;
    }

    @Override
    public ProjectResponse create(ProjectRequest projectRequest) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);

        User user = projectRequest.getManager() == null ?
                null : userRepository.findUserByUsername(projectRequest.getManager()).orElseThrow(()
                -> new BadRequestException(ErrorCode.NOT_FOUND));
        project.setManager(user);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(null);

        projectRepository.save(project);
        BeanUtils.copyProperties(project, projectResponse);

        projectResponse.setManager(user == null ? null : convertToUserDTO(user));
        return projectResponse;
    }

    @Override
    public ProjectResponse update(ProjectUpdateRequest projectRequest, Long id) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(projectRequest, project);

        User user = projectRequest.getManager() == null ?
                null : userRepository.findUserByUsername(projectRequest.getManager()).orElseThrow(()
                -> new BadRequestException(ErrorCode.NOT_FOUND));
        project.setManager(user);
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);
        BeanUtils.copyProperties(project, projectResponse);

        projectResponse.setManager(user == null ? null : convertToUserDTO(user));
        return projectResponse;
    }

    @Override
    public void delete(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        projectRepository.delete(project);
    }
    @Override
    public Page<ProjectResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return projectRepository.findAll(pageable).map(
                x -> {
                    ProjectResponse projectResponse = new ProjectResponse();
                    BeanUtils.copyProperties(x, projectResponse);

                    User user = x.getManager();
                    UserDTO userDTO = new UserDTO();
                    BeanUtils.copyProperties(user, userDTO);

                    ProjectMember projectMember = projectMemberRepository.findProjectMemberByProjectId(x.getId());
                    userDTO.setRoleInProject(projectMember.getRoleInProject());
                    projectResponse.setManager(userDTO);

                    return projectResponse;
                });
    }
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
