package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.request.ProjectUpdateRequest;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.Project;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectImplementService implements ProjectService {
    private final ProjectRepository projectRepository;

    private final GenericMapper genericMapper;

    @Autowired
    public ProjectImplementService(ProjectRepository projectRepository,   GenericMapper genericMapper) {
        this.projectRepository = projectRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public ProjectResponse getById(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectResponse projectResponse = new ProjectResponse();
        genericMapper.copy(project, projectResponse);
        return projectResponse;
    }

    @Override
    public ProjectResponse create(ProjectRequest projectRequest) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = new Project();
        genericMapper.copy(projectRequest, project);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(null);
        projectRepository.save(project);
        genericMapper.copy(project, projectResponse);
        return projectResponse;
    }

    @Override
    public ProjectResponse update(ProjectUpdateRequest projectRequest, Long id) {
        ProjectResponse projectResponse = new ProjectResponse();
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        genericMapper.copy(projectRequest, project);
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
        genericMapper.copy(project, projectResponse);
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
                    genericMapper.copy(x, projectResponse);
                    return projectResponse;
                });
    }
}
