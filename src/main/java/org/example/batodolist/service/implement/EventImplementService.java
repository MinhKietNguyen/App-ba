package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.EventRequest;
import org.example.batodolist.dto.request.EventUpdateRequest;
import org.example.batodolist.dto.response.EventResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.Event;
import org.example.batodolist.model.Project;
import org.example.batodolist.repo.EventRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventImplementService implements EventService {
    private final EventRepository eventRepository;

    private final ProjectRepository projectRepository;

    private final GenericMapper genericMapper;

    @Autowired
    public EventImplementService(EventRepository eventRepository, ProjectRepository projectRepository, GenericMapper genericMapper) {
        this.eventRepository = eventRepository;
        this.projectRepository = projectRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public EventResponse getEvents(Long id){
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        EventResponse eventResponse = new EventResponse();
        genericMapper.copy(event, eventResponse);
        if(event.getProject().getId() != null){
            Project project = projectRepository.findById(event.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
            eventResponse.setProjectName(project.getName());
        }
        return eventResponse;
    }

    @Override
    public EventResponse create(EventRequest eventRequest) {
        EventResponse eventResponse = new EventResponse();
        Event event = new Event();
        Project project = projectRepository.findProjectByName(eventRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        genericMapper.copy(eventRequest, event);
        event.setProject(project);
        eventRepository.save(event);
        genericMapper.copy(event, eventResponse);
        eventResponse.setProjectName(event.getProject().getName());
        return eventResponse;
    }

    @Override
    public EventResponse update(EventUpdateRequest eventUpdateRequest, Long id) {
        EventResponse eventResponse = new EventResponse();
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        Project project = projectRepository.findProjectByName(eventUpdateRequest.getProjectName());
        if(project == null){
            throw new BadRequestException(ErrorCode.NOT_FOUND);
        }
        genericMapper.copy(eventUpdateRequest, event);
        event.setProject(project);
        eventRepository.save(event);
        genericMapper.copy(event, eventResponse);
        eventResponse.setProjectName(event.getProject().getName());
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
                    genericMapper.copy(x, eventResponse);
                    if(x.getProject().getId() != null){
                        Project project = projectRepository.findById(x.getProject().getId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
                        eventResponse.setProjectName(project.getName());
                    }
                    return eventResponse;
                });
    }
}
