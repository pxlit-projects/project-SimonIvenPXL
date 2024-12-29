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
import be.pxl.newsarticles.service.interfaces.IDraftService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DraftService implements IDraftService {
    private final PostDraftRepository postDraftRepository;
    private final ModelMapper mapper;

    @Override
    public Draft savePostAsDraft(DraftRequest draftRequest) {
        Draft draft = Draft.builder()
                .title(draftRequest.getTitle())
                .content(draftRequest.getContent())
                .author(draftRequest.getAuthor())
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.now().withNano(0))
                .build();

        return postDraftRepository.save(draft);
    }

    @Override
    public DraftResponse getDraftById(long id) {
        Optional<Draft> draft = postDraftRepository.findById(id);

        if (draft.isEmpty()) {
            throw new ResourceNotFoundException("No post found with ID " + id);
        }

        return DraftResponse.builder()
                .title(draft.get().getTitle())
                .content(draft.get().getContent())
                .author(draft.get().getAuthor())
                .status(draft.get().getStatus())
                .savedDate(draft.get().getSavedDate())
                .build();
    }

    @Override
    public List<DraftResponse> getDrafts() {
        List<Draft> drafts = postDraftRepository.findAll();

        if (drafts.isEmpty()) {
            throw new ResourceNotFoundException("Er zijn nog geen drafts aangemaakt");
        }

        return drafts.stream().map(p -> mapper.map(p, DraftResponse.class)).toList();
    }
}