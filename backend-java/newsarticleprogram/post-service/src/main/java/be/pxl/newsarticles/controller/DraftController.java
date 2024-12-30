package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
import be.pxl.newsarticles.dto.draft.DraftResponse;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drafts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class DraftController {
    private final DraftService draftService;

    @PostMapping
    public ResponseEntity<Draft> savePostAsDraft(@RequestBody DraftRequest draftRequest) {
        try {
            return new ResponseEntity<>(draftService.savePostAsDraft(draftRequest), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<DraftResponse>> getDrafts() {
        return new ResponseEntity<>(draftService.getDrafts(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DraftResponse> getDraftById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(draftService.getDraftById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Draft> saveEditsToDraft(@PathVariable Long id, @RequestBody DraftRequest draftRequest) {
        try {
            return new ResponseEntity<>(draftService.saveEditsToDraft(id, draftRequest), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
