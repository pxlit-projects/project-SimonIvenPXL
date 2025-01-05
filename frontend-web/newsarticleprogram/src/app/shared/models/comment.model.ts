export class Comment {
  id : number;
  postId : number;
  author : string;
  content : string;
  commentDate : Date;

  constructor(id : number, postId : number, author : string, content : string, commentDate : Date) {
    this.id = id;
    this.postId = postId;
    this.author  = author;
    this.content = content;
    this.commentDate = commentDate;
  }
}
