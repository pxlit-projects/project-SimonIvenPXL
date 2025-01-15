package be.pxl.newsarticles.service;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
import be.pxl.newsarticles.dto.draft.DraftResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostDraftRepository;
import be.pxl.newsarticles.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DraftServiceTest {
    private Draft draft1;
    private Draft draft2;

    private DraftResponse response1;
    private DraftResponse response2;

    private DraftRequest request;

    @Mock
    private PostDraftRepository draftRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private DraftService draftService;

    @Captor
    private ArgumentCaptor<Draft> draftCaptor;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setUp() {
        draft1 = Draft.builder()
                .id(1L)
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        draft2 = Draft.builder()
                .id(2L)
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        response1 = DraftResponse.builder()
                .id(draft1.getId())
                .title(draft1.getTitle())
                .content(draft1.getContent())
                .author(draft1.getAuthor())
                .status(draft1.getStatus())
                .savedDate(draft1.getSavedDate())
                .build();

        response2 = DraftResponse.builder()
                .id(draft2.getId())
                .title(draft2.getTitle())
                .content(draft2.getContent())
                .author(draft2.getAuthor())
                .status(draft2.getStatus())
                .savedDate(draft2.getSavedDate())
                .build();

        request = DraftRequest.builder()
                .title("Title Request")
                .content("Content Request")
                .author("Author Request")
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.now().withNano(0))
                .build();
    }

    @Test
    public void testCreateDraft() {
        draftService.savePostAsDraft(request);
        Mockito.verify(draftRepository).save(draftCaptor.capture());
        Draft draft = draftCaptor.getValue();

        Assertions.assertEquals(request.getTitle(), draft.getTitle());
        Assertions.assertEquals(request.getContent(), draft.getContent());
        Assertions.assertEquals(request.getAuthor(), draft.getAuthor());
        Assertions.assertEquals(PostStatus.DRAFT, draft.getStatus());
    }

    @Test
    public void testGetAllDraftsWithSavedPostsShouldReturnPosts() {
        Mockito.when(draftRepository.findAll()).thenReturn(List.of(draft1, draft2));
        Mockito.when(mapper.map(draft1, DraftResponse.class)).thenReturn(response1);
        Mockito.when(mapper.map(draft2, DraftResponse.class)).thenReturn(response2);

        List<DraftResponse> draftResponses = draftService.getDrafts();

        Assertions.assertEquals(2, draftResponses.size());
        Assertions.assertEquals(draftResponses.get(0).getId(), draft1.getId());
        Assertions.assertEquals(draftResponses.get(0).getTitle(), draft1.getTitle());
        Assertions.assertEquals(draftResponses.get(0).getContent(), draft1.getContent());
        Assertions.assertEquals(draftResponses.get(0).getAuthor(), draft1.getAuthor());
        Assertions.assertEquals(draftResponses.get(0).getStatus(), draft1.getStatus());
        Assertions.assertEquals(draftResponses.get(0).getSavedDate(), draft1.getSavedDate());

        Assertions.assertEquals(draftResponses.get(1).getId(), draft2.getId());
        Assertions.assertEquals(draftResponses.get(1).getTitle(), draft2.getTitle());
        Assertions.assertEquals(draftResponses.get(1).getContent(), draft2.getContent());
        Assertions.assertEquals(draftResponses.get(1).getAuthor(), draft2.getAuthor());
        Assertions.assertEquals(draftResponses.get(1).getStatus(), draft2.getStatus());
        Assertions.assertEquals(draftResponses.get(1).getSavedDate(), draft2.getSavedDate());

        Mockito.verify(draftRepository).findAll();
    }

    @Test
    public void testGetAllDraftsWithNoneShouldThrowNotFoundException() {
        Mockito.when(draftRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> draftService.getDrafts());

        Mockito.verify(draftRepository).findAll();
    }

    @Test
    public void testGetDraftByIdAndValidIdShouldReturnPost() {
        Mockito.when(draftRepository.findById(1L)).thenReturn(Optional.of(draft1));

        DraftResponse draftResponse = draftService.getDraftById(1L);

        Assertions.assertEquals(draftResponse.getId(), draft1.getId());
        Assertions.assertEquals(draftResponse.getTitle(), draft1.getTitle());
        Assertions.assertEquals(draftResponse.getContent(), draft1.getContent());
        Assertions.assertEquals(draftResponse.getAuthor(), draft1.getAuthor());
        Assertions.assertEquals(draftResponse.getStatus(), draft1.getStatus());
        Assertions.assertEquals(draftResponse.getSavedDate(), draft1.getSavedDate());

        Mockito.verify(draftRepository).findById(1L);
    }

    @Test
    public void testGetDraftByIdAndInvalidIdShouldThrowNotFoundException() {
        Mockito.when(draftRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> draftService.getDraftById(3L));

        Mockito.verify(draftRepository).findById(3L);
    }

    @Test
    public void testSaveEditsToDraftWithValidIdShouldSavePost() {
        Mockito.when(draftRepository.findById(1L)).thenReturn(Optional.of(draft1));

        draftService.saveEditsToDraft(1L, request);

        Mockito.verify(draftRepository).save(draftCaptor.capture());
        Draft draft = draftCaptor.getValue();

        Assertions.assertEquals(draft.getTitle(), request.getTitle());
        Assertions.assertEquals(draft.getContent(), request.getContent());
        Assertions.assertEquals(draft.getAuthor(), request.getAuthor());
        Assertions.assertEquals(draft.getStatus(), request.getStatus());
        Assertions.assertEquals(draft.getSavedDate(), request.getSavedDate());

        Mockito.verify(draftRepository).findById(1L);
    }

    @Test
    public void testSaveEditsToDraftWithInvalidIdShouldThrowNotFoundException() {
        Mockito.when(draftRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> draftService.saveEditsToDraft(3L, request));

        Mockito.verify(draftRepository).findById(3L);
    }

    @Test
    public void testPublishDraftWithValidIdShouldPublishPost() {
        Mockito.when(draftRepository.findById(1L)).thenReturn(Optional.of(draft1));

        draftService.publishDraft(1L);

        Mockito.verify(draftRepository).delete(draft1);
        Mockito.verify(postRepository).save(postCaptor.capture());
        Post post = postCaptor.getValue();

        Assertions.assertEquals(post.getTitle(), draft1.getTitle());
        Assertions.assertEquals(post.getContent(), draft1.getContent());
        Assertions.assertEquals(post.getAuthor(), draft1.getAuthor());
        Assertions.assertEquals(post.getStatus(), PostStatus.PENDING);
        Assertions.assertEquals(post.getPublishedDate(), LocalDateTime.now().withNano(0));

        Mockito.verify(draftRepository).findById(1L);
    }

    @Test
    public void testPublishDraftWithInvalidIdShouldThrowNotFoundException() {
        Mockito.when(draftRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> draftService.publishDraft(3L));

        Mockito.verify(draftRepository).findById(3L);
    }
}
