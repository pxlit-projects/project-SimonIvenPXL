import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../models/post.model';
import {PostRequest} from '../models/postRequest.model';
import {Draft} from '../models/draft.model';
import {DraftRequest} from '../models/draftRequest.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  http : HttpClient =inject(HttpClient);
  api : string = environment.apiUrl;

  // POSTS
  getPosts() : Observable<Post[]> {
    return this.http.get<Post[]>(`${this.api}/post/api/posts`);
  }

  getPostDetails(id : number) : Observable<Post> {
    return this.http.get<Post>(`${this.api}/post/api/posts/${id}`);
  }

  createPost(postRequest : PostRequest) {
    return this.http.post<Post>(`${this.api}/post/api/posts`, postRequest);
  }

  saveEditsToPost(id: number, postRequest : PostRequest) {
    return this.http.put<Post>(`${this.api}/post/api/posts/${id}`, postRequest);
  }

  // DRAFTS
  getDrafts() : Observable<Draft[]> {
    return this.http.get<Draft[]>(`${this.api}/post/api/drafts`);
  }

  createDraft(draftRequest : DraftRequest) {
    return this.http.post<Draft>(`${this.api}/post/api/drafts`, draftRequest);
  }

  getDraftDetails(id : number) : Observable<Draft> {
    return this.http.get<Draft>(`${this.api}/post/api/drafts/${id}`);
  }

  saveEditsToDraft(id : number, draftRequest : DraftRequest) {
    return this.http.put(`${this.api}/post/api/drafts/${id}`, draftRequest);
  }

  publishDraft(id : number) {
    return this.http.post(`${this.api}/post/api/drafts/${id}/publish`, {});
  }
}
