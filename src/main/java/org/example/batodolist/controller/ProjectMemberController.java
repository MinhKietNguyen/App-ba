package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.response.ProjectMemberResponse;
import org.example.batodolist.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/project-member")
@RestController
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @GetMapping("")
    public ResponseEntity<ProjectMemberResponse> getProjectMember(@RequestParam Long id) {
        return ResponseEntity.ok().body(projectMemberService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<ProjectMemberResponse> postProjectMember(@RequestBody ProjectMemberRequest projectMember) {
        return ResponseEntity.ok().body(projectMemberService.create(projectMember));
    }

    @PutMapping("")
    public ResponseEntity<ProjectMemberResponse> putProjectMember(@RequestBody ProjectMemberRequest projectMember, @RequestParam Long id){
        return ResponseEntity.ok().body(projectMemberService.update(projectMember, id));
    }
    @DeleteMapping("")
    public ResponseEntity<ErrorCode> deleteProjectMember(@RequestParam Long id) {
        projectMemberService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("")
    public ResponseEntity<Page<ProjectMemberResponse>> paging(int offset, int limit) {
        return ResponseEntity.ok().body(projectMemberService.paging(offset, limit));
    }
}
