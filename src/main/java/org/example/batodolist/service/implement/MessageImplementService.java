package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.MessageRequest;
import org.example.batodolist.dto.request.MessageUpdateRequest;
import org.example.batodolist.dto.response.MessageResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.Message;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.repo.MessageRepository;
import org.example.batodolist.repo.ProjectMemberRepository;
import org.example.batodolist.repo.ProjectRepository;
import org.example.batodolist.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageImplementService implements MessageService {
    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final GenericMapper genericMapper;


    public MessageImplementService(MessageRepository messageRepository, ProjectRepository projectRepository,  ProjectMemberRepository projectMemberRepository,  GenericMapper genericMapper) {
        this.messageRepository = messageRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public MessageResponse getById(Long id){
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        MessageResponse messageResponse = new MessageResponse();
        genericMapper.copy(message, messageResponse);
        messageResponse.setProjectId(message.getProject().getId());
        messageResponse.setUserId(message.getUser().getId());
        if (message.getParent() != null) {
            messageResponse.setParentId(message.getParent().getId());
        } else {
            messageResponse.setParentId(null);
        }

        return messageResponse;
    }

    @Override
    public MessageResponse create(MessageRequest messageRequest){
        MessageResponse messageResponse = new MessageResponse();
        Message message = new Message();
        genericMapper.copy(messageRequest, message);

        Project project = projectRepository.findById(messageRequest.getProjectId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        ProjectMember sender = projectMemberRepository.findById(messageRequest.getUserId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        Message receiver = messageRepository.findById(messageRequest.getParentId()).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        if(projectMemberRepository.findProjectMemberByProject_IdAndUser_Id(project.getId(), sender.getUser().getId()) == null) {
            throw new BadRequestException(ErrorCode.THIS_PERSON_DOES_NOT_BELONG_TO_THE_PROJECT);
        }

        message.setProject(project);
        message.setUser(sender);
        message.setParent(receiver);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        genericMapper.copy(message, messageResponse);
        messageResponse.setProjectId(message.getProject().getId());
        messageResponse.setUserId(message.getUser().getId());
        if (message.getParent() != null) {
            messageResponse.setParentId(message.getParent().getId());
        } else {
            messageResponse.setParentId(null);
        }
        return messageResponse;
    }
    @Override
    public MessageResponse update(MessageUpdateRequest messageUpdateRequest, Long id){
        MessageResponse messageResponse = new MessageResponse();
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        genericMapper.copy(messageUpdateRequest, message);

        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        genericMapper.copy(message, messageResponse);
        messageResponse.setProjectId(message.getProject().getId());
        messageResponse.setUserId(message.getUser().getId());
        if (message.getParent() != null) {
            messageResponse.setParentId(message.getParent().getId());
        } else {
            messageResponse.setParentId(null);
        }
        return messageResponse;
    }
    public void delete (Long id){
        Message message = messageRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        messageRepository.delete(message);
    }
    public Page<MessageResponse> paging(int offset, int limit){
        Pageable pageable = PageRequest.of(offset, limit);
        return messageRepository.findAll(pageable).map(
                x -> {
                    MessageResponse messageResponse = new MessageResponse();
                    genericMapper.copy(x, messageResponse);
                    messageResponse.setProjectId(x.getProject().getId());
                    messageResponse.setUserId(x.getUser().getId());
                    if (x.getParent() != null) {
                        messageResponse.setParentId(x.getParent().getId());
                    } else {
                        messageResponse.setParentId(null);
                    }

                    return messageResponse;
                });
    }
}
