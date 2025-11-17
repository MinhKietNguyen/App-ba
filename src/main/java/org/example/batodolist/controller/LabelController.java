package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.LabelRequest;
import org.example.batodolist.dto.request.LabelUpdateRequest;
import org.example.batodolist.dto.response.LabelResponse;
import org.example.batodolist.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/label")
public class LabelController {
    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/detail")
    public ResponseEntity<LabelResponse> getLabel(@RequestParam Long id) {
        return ResponseEntity.ok().body(labelService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<LabelResponse> postLabel(@RequestBody LabelRequest labelRequest) {
        return ResponseEntity.ok().body(labelService.create(labelRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<LabelResponse> putLabel(@RequestBody LabelUpdateRequest labelUpdateRequest, @RequestParam Long id){
        return ResponseEntity.ok().body(labelService.update(labelUpdateRequest, id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteLabel(@RequestParam Long id) {
        labelService.delete(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }
    @GetMapping("/paging")
    public ResponseEntity<Page<LabelResponse>> paging(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body(labelService.paging(offset, limit));
    }
}
