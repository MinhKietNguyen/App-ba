package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.TaskDTO;
import org.example.batodolist.dto.request.ReminderRequest;
import org.example.batodolist.dto.request.ReminderUpdateRequest;
import org.example.batodolist.dto.response.ReminderResponse;
import org.example.batodolist.model.Reminder;
import org.example.batodolist.model.Task;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.repo.ReminderRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.repo.UserRepository;
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

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;


    public ReminderImplemetService(ReminderRepository reminderRepository, TaskRepository taskRepository,  UserRepository userRepository, ProjectRepository projectRepository) {
        this.reminderRepository = reminderRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ReminderResponse getById(Long id){
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ReminderResponse reminderResponse = new ReminderResponse();
        BeanUtils.copyProperties(reminder, reminderResponse);
        setupReminderTask(reminder.getTask(), reminderResponse);
        return reminderResponse;
    }

    @Override
    public ReminderResponse create(ReminderRequest reminderRequest) {
        ReminderResponse reminderResponse = new ReminderResponse();
        Reminder reminder = new Reminder();
        BeanUtils.copyProperties(reminderRequest, reminder);

        Task task = taskRepository.findTaskByName(reminderRequest.getTaskName()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        reminder.setTask(task);

        reminderRepository.save(reminder);
        BeanUtils.copyProperties(reminder, reminderResponse);
        setupReminderTask(reminder.getTask(), reminderResponse);
        return reminderResponse;
    }

    @Override
    public ReminderResponse update(ReminderUpdateRequest reminderUpdateRequest, Long id) {
        ReminderResponse reminderResponse = new ReminderResponse();
        Reminder reminder = reminderRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(reminderUpdateRequest, reminder);
        reminderRepository.save(reminder);
        BeanUtils.copyProperties(reminder, reminderResponse);
        setupReminderTask(reminder.getTask(), reminderResponse);
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
                    setupReminderTask(x.getTask(), reminderResponse);
                    return reminderResponse;
                });
    }
    private void setupReminderTask(Task task, ReminderResponse reminderResponse) {
        TaskDTO taskDTO = new TaskDTO();

        BeanUtils.copyProperties(task, taskDTO);

        reminderResponse.setTask(taskDTO);
    }
    private void setupReminderUser(User user, TaskDTO taskDTO){
    }
}
