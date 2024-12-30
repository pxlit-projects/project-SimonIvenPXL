export class Draft {
  id : number;
  title : string;
  content : string;
  author : string;

  constructor(id : number, title : string, content : string, author : string) {
    this.id = id;
    this.title  = title;
    this.content  = content;
    this.author  = author;
  }
}
