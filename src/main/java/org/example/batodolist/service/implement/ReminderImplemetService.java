package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ReminderRequest;
import org.example.batodolist.dto.request.ReminderUpdateRequest;
import org.example.batodolist.dto.response.ReminderResponse;
import org.example.batodolist.model.Reminder;
import org.example.batodolist.repo.ReminderRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.service.ReminderService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReminderImplemetService implements ReminderService {
    private final ReminderRepository reminderRepository;

    private final TaskRepository taskRepository;

    public ReminderImplemetService(ReminderRepository reminderRepository, TaskRepository taskRepository) {
        this.reminderRepository = reminderRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ReminderResponse getById(Long id){
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ReminderResponse reminderResponse = new ReminderResponse();
        BeanUtils.copyProperties(reminder, reminderResponse);
        return reminderResponse;
    }

    @Override
    public ReminderResponse create(ReminderRequest reminderRequest) {
        ReminderResponse reminderResponse = new ReminderResponse();
        Reminder reminder = new Reminder();
        BeanUtils.copyProperties(reminderRequest, reminder);
        reminderRepository.save(reminder);
        BeanUtils.copyProperties(reminder, reminderResponse);
        return reminderResponse;
    }

    @Override
    public ReminderResponse update(ReminderUpdateRequest reminderUpdateRequest, Long id) {
        ReminderResponse reminderResponse = new ReminderResponse();
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(reminderUpdateRequest, reminder);
        reminderRepository.save(reminder);
        BeanUtils.copyProperties(reminder, reminderResponse);
        return reminderResponse;
    }

    @Override
    public void delete(Long id) {
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        reminderRepository.delete(reminder);
    }
    @Override
    public Page<ReminderResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return reminderRepository.findAll(pageable).map(
                x -> {
                    ReminderResponse reminderResponse = new ReminderResponse();
                    BeanUtils.copyProperties(x, reminderResponse);
                    return reminderResponse;
                });
    }
}
