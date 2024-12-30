package be.pxl.newsarticles.service.interfaces;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
import be.pxl.newsarticles.dto.draft.DraftResponse;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;

import java.util.List;

public interface IDraftService {
    DraftResponse getDraftById(long id);
    Draft savePostAsDraft(DraftRequest draftRequest);
    List<DraftResponse> getDrafts();
    Draft saveEditsToDraft(long id, DraftRequest draftRequest);
}
