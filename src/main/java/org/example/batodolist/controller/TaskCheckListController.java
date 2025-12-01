package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.TaskCheckListRequest;
import org.example.batodolist.dto.request.TaskCheckListUpdateRequest;
import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskCheckListResponse;
import org.example.batodolist.dto.response.TaskResponse;
import org.example.batodolist.service.TaskCheckListService;
import org.example.batodolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/task-checklist")
@RestController
public class TaskCheckListController {
    private final TaskCheckListService taskCheckListService;

    @Autowired
    public TaskCheckListController(TaskCheckListService taskCheckListService) {
        this.taskCheckListService = taskCheckListService;
    }
    @GetMapping("/detail")
    public ResponseEntity<TaskCheckListResponse> getTaskCheckList(@RequestParam Long id) {
        return ResponseEntity.ok().body(taskCheckListService.getByID(id));
    }

    @PostMapping("/create")
    public ResponseEntity<TaskCheckListResponse> postTaskCheckList(@RequestBody TaskCheckListRequest taskCheckListRequest) {
        return ResponseEntity.ok().body(taskCheckListService.create(taskCheckListRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<TaskCheckListResponse> putTaskCheckList(@RequestBody TaskCheckListUpdateRequest taskCheckListUpdateRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(taskCheckListService.update(taskCheckListUpdateRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteTaskCheckList(@RequestParam Long id) {
        taskCheckListService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<TaskCheckListResponse>> paging(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body(taskCheckListService.paging(offset, limit));
    }
}
