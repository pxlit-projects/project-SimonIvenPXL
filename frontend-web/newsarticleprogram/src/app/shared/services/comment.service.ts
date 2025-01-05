import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {CommentRequest} from '../models/commentRequest.model';
import {Comment} from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  http : HttpClient = inject(HttpClient);
  api : string = environment.apiUrl;

  constructor() { }

  postComment(postID : number, commentRequest : CommentRequest) {
    return this.http.post<Comment>(`${this.api}/post/api/posts/${postID}/comments`, commentRequest);
  }
}
