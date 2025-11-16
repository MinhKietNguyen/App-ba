package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.EventRequest;
import org.example.batodolist.dto.request.EventUpdateRequest;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.response.EventResponse;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.model.Event;
import org.example.batodolist.model.Project;
import org.example.batodolist.repo.EventRepository;
import org.example.batodolist.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventImplementService implements EventService {
    public final EventRepository eventRepository;

    @Autowired
    public EventImplementService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventResponse getEvents(Long id){
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        EventResponse eventResponse = new EventResponse();
        BeanUtils.copyProperties(event, eventResponse);
        return eventResponse;
    }

    @Override
    public EventResponse create(EventRequest eventRequest) {
        EventResponse eventResponse = new EventResponse();
        Event event = new Event();
        BeanUtils.copyProperties(eventRequest, event);
        eventRepository.save(event);
        BeanUtils.copyProperties(event, eventResponse);
        return eventResponse;
    }

    @Override
    public EventResponse update(EventUpdateRequest eventUpdateRequest, Long id) {
        EventResponse eventResponse = new EventResponse();
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        BeanUtils.copyProperties(eventUpdateRequest, event);
        eventRepository.save(event);
        BeanUtils.copyProperties(event, eventResponse);
        return eventResponse;
    }

    @Override
    public void delete(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        eventRepository.delete(event);
    }
    @Override
    public Page<EventResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return eventRepository.findAll(pageable).map(
                x -> {
                    EventResponse eventResponse = new EventResponse();
                    BeanUtils.copyProperties(x, eventResponse);
                    return eventResponse;
                });
    }
}
