package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.response.ProjectResponse;
import org.example.batodolist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/project")
@RestController
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    private ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("")
    public ResponseEntity<ProjectResponse> getProject(@RequestParam Long id) {
        return ResponseEntity.ok().body(projectService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<ProjectResponse> postProject(@RequestBody ProjectRequest projectRequest) {
        return ResponseEntity.ok().body(projectService.create(projectRequest));
    }

    @PutMapping("")
    public ResponseEntity<ProjectResponse> putProject(@RequestBody ProjectRequest projectRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(projectService.update(projectRequest, id));
    }
    @DeleteMapping("")
    public ResponseEntity<ErrorCode> deleteProject(@RequestParam Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("")
    public ResponseEntity<Page<ProjectResponse>> paging(int offset, int limit) {
        return ResponseEntity.ok().body(projectService.paging(offset, limit));
    }
}
