export class CommentRequest {
  author : string;
  content: string;

  constructor(author: string, content: string) {
    this.author = author;
    this.content = content;
  }
}
