import {Component, inject, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {Router} from '@angular/router';
import {AuthService} from '../../shared/services/auth.service';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {PostStatus} from '../../shared/models/postStatus.model';
import {MatInputModule} from '@angular/material/input';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-post-overview',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './post-overview.component.html',
  styleUrl: './post-overview.component.css'
})
export class PostOverviewComponent implements OnInit{
  router : Router = inject(Router);
  authService : AuthService = inject(AuthService);

  postsObservable: Observable<Post[]> ;
  posts: Post[] = [];
  filteredPosts: Post[] = [];
  filterCriteria = {
    content: '',
    author: '',
    date: '',
  };
  showFilters: boolean = false;

  constructor(
    private postService : PostService,
  ) {
    this.postsObservable = this.getPosts();
  }

  ngOnInit() {
    this.postsObservable.subscribe(posts => {
      if (this.authService.isAdmin()) {
        this.posts = this.sortPostsByDate(posts);
      } else if (this.authService.isUser()) {
        this.posts = this.sortPostsByDate(posts).filter(post => post.status === PostStatus.PUBLISHED);
      }
      this.filteredPosts = this.posts;
    });
  }

  sortPostsByDate(posts : Post[]) : Post[] {
    return posts.sort((a, b) => {
      const dateA = new Date(a.publishedDate).getTime();
      const dateB = new Date(b.publishedDate).getTime();
      return dateB - dateA;
    });
  }

  trackPostId(index: number, post: Post): number {
    return post.id;
  }

  // Filters
  applyFilters(): void {
    this.filteredPosts = this.posts.filter(post => {
      const matchesContent = post.content.toLowerCase().includes(this.filterCriteria.content);
      const matchesAuthor = post.author.toLowerCase().includes(this.filterCriteria.author);
      const matchesDate = this.filterCriteria.date
        ? post.publishedDate.toString().startsWith(this.filterCriteria.date)
        : true;
      return matchesContent && matchesAuthor && matchesDate;
    });

    this.filteredPosts = this.sortPostsByDate(this.filteredPosts);
  }

  // Functionality
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
