package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.UserDTO;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.request.ProjectUpdateRequest;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.ProjectRepository;
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

    @Autowired
    public ProjectImplementService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectResponse getById(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }

    @Override
    public ProjectResponse create(ProjectRequest projectRequest) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(null);
        projectRepository.save(project);
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }

    @Override
    public ProjectResponse update(ProjectUpdateRequest projectRequest, Long id) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(projectRequest, project);
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
        BeanUtils.copyProperties(project, projectResponse);
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
                    return projectResponse;
                });
    }
}
