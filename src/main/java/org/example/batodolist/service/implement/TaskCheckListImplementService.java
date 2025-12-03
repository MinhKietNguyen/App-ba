package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.TaskDTO;
import org.example.batodolist.dto.request.TaskCheckListRequest;
import org.example.batodolist.dto.request.TaskCheckListUpdateRequest;
import org.example.batodolist.dto.response.TaskCheckListResponse;
import org.example.batodolist.model.Task;
import org.example.batodolist.model.TaskCheckList;
import org.example.batodolist.repo.TaskCheckListRepository;
import org.example.batodolist.repo.TaskRepository;
import org.example.batodolist.service.TaskCheckListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskCheckListImplementService implements TaskCheckListService {
    private final TaskCheckListRepository taskCheckListRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskCheckListImplementService(TaskCheckListRepository taskCheckListRepository, TaskRepository taskRepository) {
        this.taskCheckListRepository = taskCheckListRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskCheckListResponse getByID(Long id){
        TaskCheckList taskCheckList = taskCheckListRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskCheckListResponse taskCheckListResponse = new TaskCheckListResponse();
        BeanUtils.copyProperties(taskCheckList, taskCheckListResponse);
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskCheckList.getTask(), taskDTO);
        taskCheckListResponse.setTaskDTO(taskDTO);
        return taskCheckListResponse;
    }

    @Override
    public TaskCheckListResponse create(TaskCheckListRequest taskCheckListRequest) {
        TaskCheckListResponse taskCheckListResponse = new TaskCheckListResponse();
        TaskCheckList taskCheckList = new TaskCheckList();
        BeanUtils.copyProperties(taskCheckListRequest, taskCheckList);

        Task task = taskRepository.findTaskByName(taskCheckListRequest.getTaskName()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskCheckListRequest, taskDTO);
        taskCheckList.setTask(task);

        taskCheckList.setCreatedAt(LocalDateTime.now());
        taskCheckListRepository.save(taskCheckList);
        BeanUtils.copyProperties(taskCheckList, taskCheckListResponse);
        return taskCheckListResponse;
    }

    @Override
    public TaskCheckListResponse update(TaskCheckListUpdateRequest taskCheckListUpdateRequest, Long id) {
        TaskCheckListResponse taskCheckListResponse = new TaskCheckListResponse();
        TaskCheckList taskCheckList = taskCheckListRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(taskCheckListUpdateRequest, taskCheckList);

        Task task = taskRepository.findTaskByName(taskCheckListUpdateRequest.getTaskName()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskCheckListUpdateRequest, taskDTO);
        taskCheckList.setTask(task);

        taskCheckList.setUpdatedAt(LocalDateTime.now());
        taskCheckListRepository.save(taskCheckList);
        BeanUtils.copyProperties(taskCheckList, taskCheckListResponse);
        return taskCheckListResponse;
    }

    @Override
    public void delete(Long id) {
        TaskCheckList taskCheckList = taskCheckListRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        taskCheckListRepository.delete(taskCheckList);
    }
    @Override
    public Page<TaskCheckListResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return taskCheckListRepository.findAll(pageable).map(
                x -> {
                    TaskCheckListResponse taskCheckListResponse = new TaskCheckListResponse();
                    BeanUtils.copyProperties(x, taskCheckListResponse);
                    TaskDTO taskDTO = new TaskDTO();
                    BeanUtils.copyProperties(x.getTask(), taskDTO);
                    taskCheckListResponse.setTaskDTO(taskDTO);
                    return taskCheckListResponse;
                });
    }
}
