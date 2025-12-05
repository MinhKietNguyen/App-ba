package org.example.batodolist.controller;

import jakarta.validation.Valid;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.request.MessageUpdateRequest;
import org.example.batodolist.dto.response.MessageResponse;
import org.example.batodolist.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/detail")
    public ResponseEntity<MessageResponse> getProject(@RequestParam Long id) {
        return ResponseEntity.ok(messageService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> postProject(@Valid @RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.create(messageRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> putProject(
            @Valid @RequestBody MessageUpdateRequest messageUpdateRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok(messageService.update(messageUpdateRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteProject(@RequestParam Long id) {
        messageService.delete(id);
        return ResponseEntity.ok(ErrorCode.SUCCESS);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<MessageResponse>> paging(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(messageService.paging(offset, limit));
    }
}
