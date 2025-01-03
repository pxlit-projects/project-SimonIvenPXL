import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {PostService} from '../../shared/services/post.service';
import {PostRequest} from '../../shared/models/postRequest.model';
import {PostStatus} from '../../shared/models/postStatus.model';

@Component({
  selector: 'app-add-post',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './add-post.component.html',
  styleUrl: './add-post.component.css'
})
export class AddPostComponent implements OnInit{
  router : Router = inject(Router);
  fb: FormBuilder = inject(FormBuilder);

  title : string = "";
  content : string = "";
  author : string = "";
  status : PostStatus = PostStatus.PENDING;

  newPostForm: FormGroup = this.fb.group({
    title: ['', Validators.required],
    content: ['', Validators.required],
    author: ['', Validators.required]
  });

  constructor(
    private location : Location,
    private postService : PostService
  ) {
  }

  ngOnInit() {

  }

  savePost() {
    let title = this.newPostForm.get('title')?.value;
    let content = this.newPostForm.get('content')?.value;
    let author = this.newPostForm.get('author')?.value;
    let status = PostStatus.PENDING;

    let postRequest : PostRequest = new PostRequest(title, content, author, status);

    this.postService.createPost(postRequest).subscribe();
  }

  saveDraft() {
    let title = this.newPostForm.get('title')?.value;
    let content = this.newPostForm.get('content')?.value;
    let author = this.newPostForm.get('author')?.value;
    let status = PostStatus.DRAFT;

    let postRequest : PostRequest = new PostRequest(title, content, author, status);

    this.postService.createDraft(postRequest).subscribe();
  }

  onReturn() {
    this.location.back();
  }
}
