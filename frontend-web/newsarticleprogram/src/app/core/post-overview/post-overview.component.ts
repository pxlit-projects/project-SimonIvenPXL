import { Component, inject, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-post-overview',
  standalone: true,
  imports: [],
  templateUrl: './post-overview.component.html',
  styleUrl: './post-overview.component.css'
})
export class PostOverviewComponent implements OnInit{
  router : Router = inject(Router);

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
    })
  }

  getPosts() : Observable<Post[]>  {
    return this.postService.getPosts();
  }

  onAddPostClick() {
    this.router.navigate(['editor/posts/add']).then(r => console.log(r));
  }
}
