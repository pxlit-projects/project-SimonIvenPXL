import {PostStatus} from './postStatus.model';

export class PostRequest {
  title : string;
  content : string;
  author : string;
  status : PostStatus;
  commentIds : number[];

  constructor(title : string, content : string, author : string, status : PostStatus, commentIds: number[]) {
    this.title  = title;
    this.content  = content;
    this.author  = author;
    this.status = status;
    this.commentIds = commentIds;
  }
}
