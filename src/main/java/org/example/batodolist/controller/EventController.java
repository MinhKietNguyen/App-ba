package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.EventRequest;
import org.example.batodolist.dto.request.EventUpdateRequest;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.response.EventResponse;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.model.Event;
import org.example.batodolist.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/event")
@RestController
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/detail")
    public ResponseEntity<EventResponse> getEvent(@RequestParam Long id) {
        return ResponseEntity.ok().body(eventService.getEvents(id));
    }

    @PostMapping("/create")
    public ResponseEntity<EventResponse> postEvent(@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok().body(eventService.create(eventRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<EventResponse> putEvent(@RequestBody EventUpdateRequest eventRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(eventService.update(eventRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteEvent(@RequestParam Long id) {
        eventService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<EventResponse>> paging(int offset, int limit) {
        return ResponseEntity.ok().body(eventService.paging(offset, limit));
    }
}
