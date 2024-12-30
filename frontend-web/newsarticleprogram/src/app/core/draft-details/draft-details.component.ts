import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {PostRequest} from '../../shared/models/postRequest.model';
import {Draft} from '../../shared/models/draft.model';

@Component({
  selector: 'app-draft-details',
  standalone: true,
  imports: [],
  templateUrl: './draft-details.component.html',
  styleUrl: './draft-details.component.css'
})
export class DraftDetailsComponent implements OnInit{
  router : Router = inject(Router);

  draftId! : number;
  draft! : Draft;

  constructor(
    private postService: PostService,
    private route : ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.draftId = +params['id']; // "+" converts string to number
    });
    this.getDraftDetails(this.draftId);
  }

  getDraftDetails(id : number) {
    this.postService.getDraftDetails(id).subscribe({
      next: draft => this.draft = draft,
      error: error => console.log(error),
    });
  }

  editDraft(id : number) {
    this.router.navigate([`editor/drafts/${id}/edit`]);
  }

  saveAsPost() {
    this.postService.publishDraft(this.draftId).subscribe({
      next: draft => {
        console.log(draft);
        this.router.navigate([`editor/drafts/`]);
      }
    });
  }

  return() {
    this.router.navigate([`editor/drafts`])
  }
}
