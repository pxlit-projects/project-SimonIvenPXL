import {PostStatus} from './postStatus.model';

export class Post {
  id : number;
  title : string;
  content : string;
  author : string;
  reviewAuthor: string;
  reviewReasoning: string;
  status: PostStatus
  publishedDate : Date;
  commentIds : number[];

  constructor(id : number, title : string, content : string, author : string,
              reviewAuthor: string, reviewReasoning: string, status: PostStatus, publishedDate : Date, commentIds : number[]) {
    this.id = id;
    this.title  = title;
    this.content  = content;
    this.author  = author;
    this.reviewAuthor = reviewAuthor;
    this.reviewReasoning = reviewReasoning;
    this.status = status;
    this.publishedDate = publishedDate;
    this.commentIds = commentIds;
  }
}
