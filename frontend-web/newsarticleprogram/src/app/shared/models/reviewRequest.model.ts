export class ReviewRequest {
  reviewEditor: string;
  reasoning: string;

  constructor(reviewEditor: string, reasoning: string) {
    this.reviewEditor = reviewEditor;
    this.reasoning = reasoning;
  }
}
