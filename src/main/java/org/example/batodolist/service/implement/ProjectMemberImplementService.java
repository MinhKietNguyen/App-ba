package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.ProjectDTO;
import org.example.batodolist.dto.UserDTO;
import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.request.ProjectMemberUpdateRequest;
import org.example.batodolist.dto.response.ProjectMemberResponse;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.ProjectMemberRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.UserRepository;
import org.example.batodolist.service.ProjectMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberImplementService implements ProjectMemberService {
    private final ProjectMemberRepository  projectMemberRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    @Autowired
    public ProjectMemberImplementService(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository,  UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectMemberResponse getById(Long id){
        ProjectMember projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        setupProjectMember(projectMember.getProject(), projectMember.getUser(), projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public ProjectMemberResponse create(ProjectMemberRequest projectMemberRequest) {
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        ProjectMember projectMember = new ProjectMember();
        BeanUtils.copyProperties(projectMemberRequest, projectMember);

        Project project = projectRepository.findProjectByName(projectMemberRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        User user = userRepository.findUserByUsername(projectMemberRequest.getMemberName()).
                orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        projectMember.setProject(project);
        projectMember.setUser(user);

        projectMemberRepository.save(projectMember);
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        setupProjectMember(projectMember.getProject(), projectMember.getUser(), projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public ProjectMemberResponse update(ProjectMemberUpdateRequest projectMemberUpdateRequest, Long id) {
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        ProjectMember projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(projectMemberUpdateRequest, projectMember);

        Project project = projectRepository.findProjectByName(projectMemberUpdateRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        User user = userRepository.findUserByUsername(projectMemberUpdateRequest.getMemberName()).
                orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        projectMember.setProject(project);
        projectMember.setUser(user);

        projectMemberRepository.save(projectMember);
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        setupProjectMember(projectMember.getProject(), projectMember.getUser(), projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public void delete(Long id) {
        ProjectMember projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        projectMemberRepository.delete(projectMember);
    }
    @Override
    public Page<ProjectMemberResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return projectMemberRepository.findAll(pageable).map(
                x -> {
                    ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
                    BeanUtils.copyProperties(x, projectMemberResponse);
                    setupProjectMember(x.getProject(), x.getUser(), projectMemberResponse);
                    return projectMemberResponse;
                });
    }

    private void setupProjectMember(Project project, User user, ProjectMemberResponse projectMemberResponse) {
        ProjectDTO projectDTO = new ProjectDTO();
        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(project, projectDTO);
        BeanUtils.copyProperties(user, userDTO);

        projectMemberResponse.setProject(projectDTO);
        projectMemberResponse.setUser(userDTO);
    }
}
