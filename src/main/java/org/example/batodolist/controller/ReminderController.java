package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ReminderRequest;
import org.example.batodolist.dto.request.ReminderUpdateRequest;
import org.example.batodolist.dto.response.ReminderResponse;
import org.example.batodolist.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/remind")
public class ReminderController {
    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService){
        this.reminderService = reminderService;
    }

    @GetMapping("/detail")
    public ResponseEntity<ReminderResponse> getReminder(@RequestParam Long id) {
        return ResponseEntity.ok().body(reminderService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ReminderResponse> postReminder(@RequestBody ReminderRequest reminderRequest) {
        return ResponseEntity.ok().body(reminderService.create(reminderRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<ReminderResponse> putReminder(@RequestBody ReminderUpdateRequest reminderUpdateRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(reminderService.update(reminderUpdateRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteReminder(@RequestParam Long id) {
        reminderService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<ReminderResponse>> paging(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body(reminderService.paging(offset, limit));
    }
}
