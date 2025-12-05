package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.LabelRequest;
import org.example.batodolist.dto.request.LabelUpdateRequest;
import org.example.batodolist.dto.response.LabelResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.Label;
import org.example.batodolist.repo.LabelRepository;
import org.example.batodolist.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LabelImplementService implements LabelService {
    private final LabelRepository labelRepository;

    private final GenericMapper genericMapper;

    @Autowired
    public LabelImplementService(LabelRepository labelRepository,  GenericMapper genericMapper) {
        this.labelRepository = labelRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public LabelResponse getById(Long id){
        Label label = labelRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        LabelResponse labelResponse = new LabelResponse();
        genericMapper.copy(label, labelResponse);
        return labelResponse;
    }

    @Override
    public LabelResponse create(LabelRequest labelRequest) {
        LabelResponse labelResponse = new LabelResponse();
        Label label = new Label();
        genericMapper.copy(labelRequest, label);
        label.setCreatedAt(LocalDateTime.now());
        labelRepository.save(label);
        genericMapper.copy(label, labelResponse);
        return labelResponse;
    }

    @Override
    public LabelResponse update(LabelUpdateRequest labelUpdateRequest, Long id) {
        LabelResponse labelResponse = new LabelResponse();
        Label label = labelRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        genericMapper.copy(labelUpdateRequest, label);
        labelRepository.save(label);
        genericMapper.copy(label, labelResponse);
        return labelResponse;
    }

    @Override
    public void delete(Long id) {
        Label label = labelRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        labelRepository.delete(label);
    }
    @Override
    public Page<LabelResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return labelRepository.findAll(pageable).map(
                x -> {
                    LabelResponse labelResponse = new LabelResponse();
                    genericMapper.copy(x, labelResponse);
                    return labelResponse;
                });
    }
}
