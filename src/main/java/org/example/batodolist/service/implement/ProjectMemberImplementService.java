package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.response.ProjectMemberResponse;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.repo.ProjectMemberRepository;
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

    @Autowired
    public ProjectMemberImplementService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    public ProjectMemberResponse getById(Long id){
        ProjectMember projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public ProjectMemberResponse create(ProjectMemberRequest projectMemberRequest) {
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        ProjectMember projectMember = new ProjectMember();
        BeanUtils.copyProperties(projectMemberRequest, projectMember);
        projectMemberRepository.save(projectMember);
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public ProjectMemberResponse update(ProjectMemberRequest projectMemberRequest, Long id) {
        ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
        ProjectMember projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(projectMemberRequest, projectMember);
        projectMemberRepository.save(projectMember);
        BeanUtils.copyProperties(projectMember, projectMemberResponse);
        return projectMemberResponse;
    }

    @Override
    public void delete(Long id) {
        projectMemberRepository.deleteById(id);
    }
    @Override
    public Page<ProjectMemberResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return projectMemberRepository.findAll(pageable).map(
                x -> {
                    ProjectMemberResponse projectMemberResponse = new ProjectMemberResponse();
                    BeanUtils.copyProperties(x, projectMemberResponse);
                    return projectMemberResponse;
                });
    }
}
