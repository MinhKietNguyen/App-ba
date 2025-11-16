package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.TaskRequest;
import org.example.batodolist.dto.request.TaskUpdateRequest;
import org.example.batodolist.dto.response.TaskResponse;
import org.example.batodolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/detail")
    public ResponseEntity<TaskResponse> getService(@RequestParam Long id) {
        return ResponseEntity.ok().body(taskService.getByID(id));
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> postService(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok().body(taskService.create(taskRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<TaskResponse> putService(@RequestBody TaskUpdateRequest taskUpdateRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(taskService.update(taskUpdateRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteService(@RequestParam Long id) {
        taskService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<TaskResponse>> paging(int offset, int limit) {
        return ResponseEntity.ok().body(taskService.paging(offset, limit));
    }
}
