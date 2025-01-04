import {ReviewStatus} from './reviewStatus.model';

export class Review {
  id: number;
  postId: number;
  reviewEditor: string;
  decision: ReviewStatus;
  reasoning: string;
  reviewedDate: Date;

  constructor(id: number, postId: number, reviewEditor: string, decision: ReviewStatus,
              reasoning: string, reviewedDate: Date) {
    this.id = id;
    this.postId = postId;
    this.reviewEditor = reviewEditor;
    this.reasoning = reasoning;
    this.decision = decision;
    this.reviewedDate = reviewedDate;
  }
}
