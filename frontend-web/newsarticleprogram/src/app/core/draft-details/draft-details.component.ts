import {Component, inject} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';

@Component({
  selector: 'app-draft-details',
  standalone: true,
  imports: [],
  templateUrl: './draft-details.component.html',
  styleUrl: './draft-details.component.css'
})
export class DraftDetailsComponent {
  router : Router = inject(Router);

  draftId! : number;
  draft! : Post;

  constructor(
    private postService: PostService,
    private route : ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.draftId = +params['id']; // "+" converts string to number
    });
    this.GetDraftDetails(this.draftId);
  }

  GetDraftDetails(id : number) {
    this.postService.getPostDetails(id).subscribe({
      next: post => this.draft = post,
      error: error => console.log(error),
    });
  }

  EditDraft(id : number) {
    this.router.navigate([`editor/drafts/${id}/edit`]);
  }
}
