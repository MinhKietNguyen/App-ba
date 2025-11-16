package org.example.batodolist.service;

import org.example.batodolist.dto.request.LabelRequest;
import org.example.batodolist.dto.request.LabelUpdateRequest;
import org.example.batodolist.dto.response.LabelResponse;
import org.springframework.data.domain.Page;

public interface LabelService {
    LabelResponse getById(Long id);
    LabelResponse create(LabelRequest labelRequest);
    LabelResponse update(LabelUpdateRequest labelUpdateRequest, Long id);
    void delete (Long id);
    Page<LabelResponse> paging(int offset, int limit);
}
