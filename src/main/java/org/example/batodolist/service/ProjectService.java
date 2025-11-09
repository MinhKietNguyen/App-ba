package org.example.batodolist.service;

import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.response.ProjectMemberResponse;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.model.ProjectMember;
import org.springframework.data.domain.Page;

public interface ProjectService {
    public ProjectResponse getById(Long id);
    public ProjectResponse create(ProjectRequest projectRequest);

    public ProjectResponse update(ProjectRequest projectRequest, Long id);

    public void delete(Long id);

    public Page<ProjectResponse> paging(int offset, int limit);
}
