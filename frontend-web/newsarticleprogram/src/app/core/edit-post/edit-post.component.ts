import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {PostRequest} from '../../shared/models/postRequest.model';
import {NgIf} from '@angular/common';
import {PostStatus} from '../../shared/models/postStatus.model';

@Component({
  selector: 'app-edit-post',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css'
})
export class EditPostComponent implements OnInit {
  router : Router = inject(Router);
  fb: FormBuilder = inject(FormBuilder);

  postId! : number;
  post! : Post;

  editPostForm = this.fb.group({
    title: ['', Validators.required],
    content: ['', Validators.required]
  });

  constructor(
    private postService: PostService,
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

        this.editPostForm = this.fb.group({
          title: [this.post.title, Validators.required],
          content: [this.post.content, Validators.required]
        })
      },
      error: error => console.log(error),
    });
  }

  saveChanges() {
    let title : string = this.editPostForm.get('title')!.value!;
    let content : string = this.editPostForm.get('content')!.value!;
    let author : string = this.post.author;
    let status : PostStatus = PostStatus.PENDING;
    let commentIds = this.post.commentIds;

    let postRequest: PostRequest = new PostRequest(title, content, author, status, commentIds);

    this.postService.saveEditsToPost(this.postId, postRequest).subscribe({
      next: post => {
        console.log(post);
        this.router.navigate([`editor/posts/${this.postId}`]);
      },
      error: error => console.log(error),
    });
  }

  return() {
    this.router.navigate([`editor/posts/${this.postId}`]);
  }
}
