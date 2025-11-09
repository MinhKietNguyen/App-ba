package org.example.batodolist.service;

import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.response.ProjectMemberResponse;
import org.example.batodolist.model.ProjectMember;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectMemberService {
    public ProjectMemberResponse getById(Long id);
    public ProjectMemberResponse create(ProjectMemberRequest projectMemberRequest);

    public ProjectMemberResponse update(ProjectMemberRequest projectMemberRequest, Long id);

    public void delete(Long id);

    public Page<ProjectMemberResponse> paging(int offset, int limit);
}
