package org.example.batodolist.service;

import org.example.batodolist.dto.request.TaskCheckListRequest;
import org.example.batodolist.dto.request.TaskCheckListUpdateRequest;
import org.example.batodolist.dto.response.TaskCheckListResponse;
import org.springframework.data.domain.Page;

public interface TaskCheckListService {
    TaskCheckListResponse getByID(Long id);
    TaskCheckListResponse create(TaskCheckListRequest taskCheckListRequest);
    TaskCheckListResponse update(TaskCheckListUpdateRequest taskCheckListUpdateRequest, Long id);
    void delete (Long id);

    Page<TaskCheckListResponse> paging(int offset, int limit);
}
