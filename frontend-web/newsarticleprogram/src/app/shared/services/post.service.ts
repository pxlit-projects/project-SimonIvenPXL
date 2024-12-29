import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../models/post.model';
import {PostRequest} from '../models/postRequest.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  http : HttpClient =inject(HttpClient);

  getPosts() : Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8081/posts`);
  }

  getDrafts() : Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8081/drafts`);
  }

  createPost(postRequest : PostRequest) {
    return this.http.post<Post>(`http://localhost:8081/posts`, postRequest);
  }

  createDraft(postRequest : PostRequest) {
    return this.http.post<Post>(`http://localhost:8081/drafts`, postRequest);
  }
}
