import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PostService} from '../../shared/services/post.service';
import {Post} from '../../shared/models/post.model';
import {AuthService} from '../../shared/services/auth.service';

import {PostStatus} from '../../shared/models/postStatus.model';
import {Comment} from '../../shared/models/comment.model';
import {CommentService} from '../../shared/services/comment.service';
import {FormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {PostRequest} from '../../shared/models/postRequest.model';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [
    FormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule
],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css'
})
export class PostDetailsComponent implements OnInit{
  router : Router = inject(Router);
  authService : AuthService = inject(AuthService);

  postId! : number;
  post! : Post;

  comments : Comment[] = [];

  isEditing: { [key: number]: boolean } = {};
  editedContent: { [key: number]: string } = {};

  constructor(
    private postService: PostService,
    private route : ActivatedRoute,
    private commentService: CommentService,
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.postId = +params['id']; // "+" converts string to number
    });
    this.getPostDetails(this.postId);
    this.commentService.getCommentsForPost(this.postId).subscribe(comments => {
      this.comments = comments;
      console.log(this.comments);
    });
  }

  getPostDetails(id : number) {
    this.postService.getPostDetails(id).subscribe({
      next: post => {
        this.post = post;
      },
      error: error => console.log(error),
    });
  }

  trackComment(index: number, comment: Comment): number {
    return comment.id;
  }

  toggleEdit(commentId: number) {
    this.isEditing[commentId] = !this.isEditing[commentId];
    const comment = this.comments.find(comment => comment.id === commentId);
    if (comment && this.isEditing[commentId]) {
      this.editedContent[commentId] = comment.content;
    }
  }

  editPost(id : number) {
    this.router.navigate([`editor/posts/${id}/edit`]);
  }

  publishPostOfficially() {
    let postRequest : PostRequest = new PostRequest(this.post.title, this.post.content, this.post.author, PostStatus.PUBLISHED, this.post.commentIds)
    this.postService.saveEditsToPost(this.postId, postRequest).subscribe({});
  }

  reviewPost(id: number) {
    this.router.navigate([`editor/posts/${id}/review`]);
  }

  addComment(id : number) {
    this.router.navigate([`posts/${id}/comment`]);
  }

  editComment(commentId : number) {
    const updatedContent = this.editedContent[commentId];

    this.commentService.updateComment(this.postId, commentId, updatedContent).subscribe({
      next: () => {
        this.toggleEdit(commentId);
        this.getPostDetails(this.postId);
      },
      error: error => console.log(error),
    })
  }

  deleteComment(commentId: number) {
    this.commentService.deleteCommentFromPost(this.postId, commentId).subscribe({
      next: () => {
        this.commentService.getCommentsForPost(this.postId).subscribe(comments => {
          this.comments = comments;
          console.log(this.comments);
        });
      },
      error: error => console.error('Error deleting comment:', error),
    });
  }

  return() {
    this.router.navigate([`posts`])
  }

  protected readonly PostStatus = PostStatus;
}
