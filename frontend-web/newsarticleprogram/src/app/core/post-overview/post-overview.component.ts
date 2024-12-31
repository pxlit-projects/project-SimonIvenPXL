import { Component, inject, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {Router} from '@angular/router';
import {AuthService} from '../../shared/services/auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-post-overview',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './post-overview.component.html',
  styleUrl: './post-overview.component.css'
})
export class PostOverviewComponent implements OnInit{
  router : Router = inject(Router);
  authService : AuthService = inject(AuthService);

  postsObservable: Observable<Post[]> ;
  posts: Post[] = [];

  constructor(
    private postService : PostService,
  ) {
    this.postsObservable = this.getPosts();
  }

  ngOnInit() {
    this.postsObservable.subscribe(posts => {
      this.posts = posts;
    });
  }

  getPosts() : Observable<Post[]>  {
    return this.postService.getPosts();
  }

  getPostDetails(id : number) {
    this.router.navigate([`posts/${id}`]);
  }

  addPost() {
    if (this.authService.isAdmin()) {
      this.router.navigate(['editor/posts/add']).then(r => console.log(r));
    } else {
      console.log("No permission");
    }
  }

  goToDrafts() {
    this.router.navigate(['editor/drafts']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
