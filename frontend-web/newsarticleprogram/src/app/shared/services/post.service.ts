import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../models/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  http : HttpClient =inject(HttpClient);

  getPosts() : Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8081/posts`);
  }
}
