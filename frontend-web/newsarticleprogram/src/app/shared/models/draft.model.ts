export class Draft {
  id : number;
  title : string;
  content : string;
  author : string;
  savedDate : Date;

  constructor(id : number, title : string, content : string, author : string, savedDate : Date) {
    this.id = id;
    this.title  = title;
    this.content  = content;
    this.author  = author;
    this.savedDate = savedDate;
  }
}
