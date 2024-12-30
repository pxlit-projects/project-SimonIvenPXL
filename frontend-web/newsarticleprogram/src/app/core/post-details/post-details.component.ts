import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PostService} from '../../shared/services/post.service';
import {Post} from '../../shared/models/post.model';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css'
})
export class PostDetailsComponent implements OnInit{
  router : Router = inject(Router);

  postId! : number;
  post! : Post;

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
      next: post => this.post = post,
      error: error => console.log(error),
    });
  }

  editPost(id : number) {
    this.router.navigate([`editor/posts/${id}/edit`]);
  }

  return() {
    this.router.navigate([`editor/posts`])
  }

}
