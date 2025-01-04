import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Review} from '../models/review.model';
import {ReviewRequest} from '../models/reviewRequest.model';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  http : HttpClient = inject(HttpClient);
  api : string = environment.apiUrl;

  constructor() { }

  approvePost(postId : number, reviewRequest : ReviewRequest) {
    return this.http.post<Review>(`${this.api}/review/api/reviews/${postId}/approve`, reviewRequest);
  }

  rejectPost(postId : number, reviewRequest : ReviewRequest) {
    return this.http.post<Review>(`${this.api}/review/api/reviews/${postId}/reject`, reviewRequest);
  }
}
