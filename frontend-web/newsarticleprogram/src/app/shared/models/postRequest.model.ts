import {PostStatus} from './postStatus.model';

export class PostRequest {
  title : string;
  content : string;
  author : string;
  status : PostStatus

  constructor(title : string, content : string, author : string, status : PostStatus) {
    this.title  = title;
    this.content  = content;
    this.author  = author;
    this.status = status;
  }
}
