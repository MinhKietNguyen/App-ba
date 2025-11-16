package org.example.batodolist.service;


import org.example.batodolist.dto.request.EventRequest;
import org.example.batodolist.dto.request.EventUpdateRequest;
import org.example.batodolist.dto.response.EventResponse;
import org.springframework.data.domain.Page;

public interface EventService {
    EventResponse getEvents(Long id);
    EventResponse create(EventRequest eventRequest);
    EventResponse update(EventUpdateRequest eventRequest, Long id);
    void delete (Long id);

    Page<EventResponse> paging(int offset, int limit);
}
