import {Component, OnInit, inject} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../shared/services/auth.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Location, NgIf} from '@angular/common';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {ReviewRequest} from '../../shared/models/reviewRequest.model';
import {ReviewService} from '../../shared/services/review.service';

@Component({
  selector: 'app-add-review',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './add-review.component.html',
  styleUrl: './add-review.component.css'
})
export class AddReviewComponent implements OnInit{
  router : Router = inject(Router);
  fb : FormBuilder = inject(FormBuilder);
  authService : AuthService = inject(AuthService);

  postId! : number;
  post! : Post;

  newReviewForm: FormGroup = this.fb.group({
    reasoning: ['', Validators.required]
  });

  constructor(
    private location : Location,
    private postService : PostService,
    private reviewService : ReviewService,
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

  approvePost() {
    let reasoning : string;
    if (this.newReviewForm.get('reasoning')?.value == "" || this.newReviewForm.get('reasoning')?.value == null) {
      reasoning = "No reason given";
    } else {
      reasoning = this.newReviewForm.get('reasoning')?.value;
    }

    let editor = this.authService.getUser().username;

    let reviewRequest : ReviewRequest = new ReviewRequest(editor, reasoning);

    console.log("Approved!", reasoning);

    this.reviewService.approvePost(this.postId, reviewRequest).subscribe();
    this.router.navigate(['editor/posts'], {queryParams: {updated: new Date().getTime()}});
  }

  rejectPost() {
    let reasoning : string;
    if (this.newReviewForm.get('reasoning')?.value == "" || this.newReviewForm.get('reasoning')?.value == null) {
      reasoning = "No reason given";
    } else {
      reasoning = this.newReviewForm.get('reasoning')?.value;
    }

    let editor = this.authService.getUser().username;

    let reviewRequest : ReviewRequest = new ReviewRequest(editor, reasoning);

    console.log("Rejected!", reviewRequest.reasoning);

    this.reviewService.rejectPost(this.postId, reviewRequest).subscribe();
    this.router.navigate(['editor/posts'], {queryParams: {updated: new Date().getTime()}});
  }

  onReturn() {
    this.location.back();
  }
}
