package be.pxl.newsarticles.service;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
import be.pxl.newsarticles.dto.draft.DraftResponse;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostDraftRepository;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.newsarticles.service.interfaces.IDraftService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DraftService implements IDraftService {
    private final PostDraftRepository postDraftRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(DraftService.class);


    @Override
    public Draft savePostAsDraft(DraftRequest draftRequest) {
        logger.info("Saving post as draft");
        Draft draft = Draft.builder()
                .title(draftRequest.getTitle())
                .content(draftRequest.getContent())
                .author(draftRequest.getAuthor())
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.now().withNano(0))
                .build();

        logger.info("Draft saved!");
        return postDraftRepository.save(draft);
    }

    @Override
    public DraftResponse getDraftById(long id) {
        logger.info("Getting draft by id: {}", id);
        Optional<Draft> draft = postDraftRepository.findById(id);

        if (draft.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("No post found with ID " + id);
        }


        logger.info("Draft found: {}", draft.get());
        return DraftResponse.builder()
                .id(draft.get().getId())
                .title(draft.get().getTitle())
                .content(draft.get().getContent())
                .author(draft.get().getAuthor())
                .status(draft.get().getStatus())
                .savedDate(draft.get().getSavedDate())
                .build();
    }

    @Override
    public List<DraftResponse> getDrafts() {
        logger.info("Getting drafts");
        List<Draft> drafts = postDraftRepository.findAll();

        if (drafts.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("Er zijn nog geen drafts aangemaakt");
        }

        logger.info("Drafts found!");
        return drafts.stream().map(p -> mapper.map(p, DraftResponse.class)).toList();
    }

    @Override
    public Draft saveEditsToDraft(long id, DraftRequest draftRequest) {
        logger.info("Saving edit to draft");
        Draft draft = postDraftRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + id));

        draft.setTitle(draftRequest.getTitle());
        draft.setContent(draftRequest.getContent());
        draft.setAuthor(draftRequest.getAuthor());
        draft.setStatus(PostStatus.DRAFT);
        draft.setSavedDate(LocalDateTime.now().withNano(0));

        logger.info("Draft saved!");
        return postDraftRepository.save(draft);
    }

    @Override
    public Post publishDraft(long id) {
        logger.info("Publishing draft");
        Draft draft = postDraftRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + id));

        Post response =  Post.builder()
                .title(draft.getTitle())
                .content(draft.getContent())
                .author(draft.getAuthor())
                .status(PostStatus.PENDING)
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        postDraftRepository.delete(draft);
        logger.info("Draft published!");
        return postRepository.save(response);
    }
}
