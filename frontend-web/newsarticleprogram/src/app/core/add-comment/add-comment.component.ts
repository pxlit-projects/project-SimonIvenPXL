import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../shared/services/auth.service';
import {Post} from '../../shared/models/post.model';
import {Location} from '@angular/common';
import {PostService} from '../../shared/services/post.service';
import {ReviewService} from '../../shared/services/review.service';
import {CommentService} from '../../shared/services/comment.service';
import {CommentRequest} from '../../shared/models/commentRequest.model';

@Component({
  selector: 'app-add-comment',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './add-comment.component.html',
  styleUrl: './add-comment.component.css'
})
export class AddCommentComponent implements OnInit{
  router : Router = inject(Router);
  fb : FormBuilder = inject(FormBuilder);
  authService : AuthService = inject(AuthService);

  postId! : number;
  post! : Post;

  newCommentForm: FormGroup = this.fb.group({
    comment: ['', Validators.required]
  });

  constructor(
    private location : Location,
    private postService : PostService,
    private commentService : CommentService,
    private route : ActivatedRoute,
  ) {

  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.postId = +params['id']; // "+" converts string to number
    });
    this.getPostDetails(this.postId);
  }

  getPostDetails(id : number) {
    this.postService.getPostDetails(id).subscribe({
      next: post => {
        this.post = post;
        console.log(post);
      },
      error: error => console.log(error),
    });
  }

  postComment() {
    let comment : string;
    if (this.newCommentForm.get('comment')?.value == "" || this.newCommentForm.get('comment')?.value == null) {
      console.log("Comment can't be empty");
      return;
    } else {
      comment = this.newCommentForm.get('comment')?.value;
    }

    let author = this.authService.getUser().username;

    let commentRequest : CommentRequest = new CommentRequest(author, comment);

    console.log(commentRequest);

    this.commentService.postComment(this.postId, commentRequest).subscribe();
    this.router.navigate([`posts/${this.postId}`]);
  }

  onReturn() {
    this.location.back();
  }
}
