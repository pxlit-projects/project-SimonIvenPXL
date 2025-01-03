import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../models/post.model';
import {PostRequest} from '../models/postRequest.model';
import {Draft} from '../models/draft.model';
import {DraftRequest} from '../models/draftRequest.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  http : HttpClient =inject(HttpClient);

  // POSTS
  getPosts() : Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8081/api/posts`);
  }

  getPostDetails(id : number) : Observable<Post> {
    return this.http.get<Post>(`http://localhost:8081/api/posts/${id}`);
  }

  createPost(postRequest : PostRequest) {
    return this.http.post<Post>(`http://localhost:8081/api/posts`, postRequest);
  }

  saveEditsToPost(id: number, postRequest : PostRequest) {
    return this.http.put<Post>(`http://localhost:8081/api/posts/${id}`, postRequest);
  }

  // DRAFTS
  getDrafts() : Observable<Draft[]> {
    return this.http.get<Draft[]>(`http://localhost:8081/api/drafts`);
  }

  createDraft(draftRequest : DraftRequest) {
    return this.http.post<Draft>(`http://localhost:8081/api/drafts`, draftRequest);
  }

  getDraftDetails(id : number) : Observable<Draft> {
    return this.http.get<Draft>(`http://localhost:8081/api/drafts/${id}`);
  }

  saveEditsToDraft(id : number, draftRequest : DraftRequest) {
    return this.http.put(`http://localhost:8081/api/drafts/${id}`, draftRequest);
  }

  publishDraft(id : number) {
    return this.http.post(`http://localhost:8081/api/drafts/${id}/publish`, {});
  }
}
