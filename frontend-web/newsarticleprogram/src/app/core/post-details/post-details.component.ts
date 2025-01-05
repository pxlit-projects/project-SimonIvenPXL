import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PostService} from '../../shared/services/post.service';
import {Post} from '../../shared/models/post.model';
import {AuthService} from '../../shared/services/auth.service';
import {NgIf} from '@angular/common';
import {PostStatus} from '../../shared/models/postStatus.model';
import {Comment} from '../../shared/models/comment.model';
import {CommentService} from '../../shared/services/comment.service';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [
    NgIf
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

  editPost(id : number) {
    this.router.navigate([`editor/posts/${id}/edit`]);
  }

  reviewPost(id: number) {
    this.router.navigate([`editor/posts/${id}/review`]);
  }

  addComment(id : number) {
    this.router.navigate([`posts/${id}/comment`]);
  }

  return() {
    this.router.navigate([`posts`])
  }

  protected readonly PostStatus = PostStatus;
}
