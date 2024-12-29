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

  draftsObservable: Observable<Post[]> ;
  drafts: Post[] = []

  constructor(
    private postService : PostService,
  ) {
    this.postsObservable = this.getPosts();
    this.draftsObservable = this.getDrafts();
  }

  ngOnInit() {
    this.postsObservable.subscribe(posts => {
      this.posts = posts;
    });

    this.draftsObservable.subscribe(drafts => {
      this.drafts = drafts;
    })
  }

  getPosts() : Observable<Post[]>  {
    return this.postService.getPosts();
  }

  getDrafts() : Observable<Post[]>  {
    return this.postService.getDrafts();
  }

  onAddPostClick() {
    this.router.navigate(['editor/posts/add']).then(r => console.log(r));
  }
}
