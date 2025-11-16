package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProjectRequest;
import org.example.batodolist.dto.request.ProjectUpdateRequest;
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

    @GetMapping("/detail")
    public ResponseEntity<ProjectResponse> getProject(@RequestParam Long id) {
        return ResponseEntity.ok().body(projectService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> postProject(@RequestBody ProjectRequest projectRequest) {
        return ResponseEntity.ok().body(projectService.create(projectRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<ProjectResponse> putProject(@RequestBody ProjectUpdateRequest projectRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(projectService.update(projectRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteProject(@RequestParam Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<ProjectResponse>> paging(int offset, int limit) {
        return ResponseEntity.ok().body(projectService.paging(offset, limit));
    }
}
