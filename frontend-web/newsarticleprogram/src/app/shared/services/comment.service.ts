import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {CommentRequest} from '../models/commentRequest.model';
import {Comment} from '../models/comment.model';
import {Post} from '../models/post.model';

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

  getCommentsForPost(postId: number) {
    return this.http.get<Comment[]>(`${this.api}/post/api/posts/${postId}/comments`);
  }

  updateComment(postId: number, commentId: number, comment: string) {
    return this.http.put(`${this.api}/post/api/posts/${postId}/comments/${commentId}`, comment);
  }

  deleteCommentFromPost(postId: number, commentId: number) {
    return this.http.delete(`${this.api}/post/api/posts/${postId}/comments/${commentId}`);
  }
}
