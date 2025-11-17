package org.example.batodolist.service;

import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.request.ProjectUpdateRequest;
import org.example.batodolist.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;

public interface ProjectService {
    public ProjectResponse getById(Long id);
    public ProjectResponse create(ProjectRequest projectRequest);

    public ProjectResponse update(ProjectUpdateRequest projectRequest, Long id);

    public void delete(Long id);

    public Page<ProjectResponse> paging(int offset, int limit);
}
