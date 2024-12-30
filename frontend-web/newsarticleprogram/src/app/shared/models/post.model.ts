export class Post {
  id : number;
  title : string;
  content : string;
  author : string;
  publishedDate : Date;

  constructor(id : number, title : string, content : string, author : string, publishedDate : Date) {
    this.id = id;
    this.title  = title;
    this.content  = content;
    this.author  = author;
    this.publishedDate = publishedDate;
  }
}
