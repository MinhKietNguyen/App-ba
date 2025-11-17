package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectMemberRequest;
import org.example.batodolist.dto.request.ProjectMemberUpdateRequest;
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

    @GetMapping("/detail")
    public ResponseEntity<ProjectMemberResponse> getProjectMember(@RequestParam Long id) {
        return ResponseEntity.ok().body(projectMemberService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectMemberResponse> postProjectMember(@RequestBody ProjectMemberRequest projectMember) {
        return ResponseEntity.ok().body(projectMemberService.create(projectMember));
    }

    @PutMapping("/update")
    public ResponseEntity<ProjectMemberResponse> putProjectMember(@RequestBody ProjectMemberUpdateRequest projectMemberUpdateRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(projectMemberService.update(projectMemberUpdateRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteProjectMember(@RequestParam Long id) {
        projectMemberService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<ProjectMemberResponse>> paging(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body(projectMemberService.paging(offset, limit));
    }
}
