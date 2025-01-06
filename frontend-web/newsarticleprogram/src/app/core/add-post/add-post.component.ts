import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {PostService} from '../../shared/services/post.service';
import {PostRequest} from '../../shared/models/postRequest.model';
import {PostStatus} from '../../shared/models/postStatus.model';
import {AuthService} from '../../shared/services/auth.service';

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
  authService : AuthService = inject(AuthService);

  title : string = "";
  content : string = "";
  author : string = "";

  newPostForm: FormGroup = this.fb.group({
    title: ['', Validators.required],
    content: ['', Validators.required]
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
    let author = this.authService.getUser().username;
    let status = PostStatus.PENDING;
    let commentIds : number[] = [];

    let postRequest : PostRequest = new PostRequest(title, content, author, status, commentIds);

    this.postService.createPost(postRequest).subscribe();
    this.router.navigate(['editor/posts']);
  }

  saveDraft() {
    let title = this.newPostForm.get('title')?.value;
    let content = this.newPostForm.get('content')?.value;
    let author = this.authService.getUser().username;
    let status = PostStatus.DRAFT;
    let commentIds: number[] = [];

    let postRequest : PostRequest = new PostRequest(title, content, author, status, commentIds);

    this.postService.createDraft(postRequest).subscribe();
    this.router.navigate(['editor/drafts']);
  }

  onReturn() {
    this.location.back();
  }
}
