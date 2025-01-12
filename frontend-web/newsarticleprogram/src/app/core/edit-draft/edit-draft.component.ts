import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Post} from '../../shared/models/post.model';
import {PostService} from '../../shared/services/post.service';
import {PostRequest} from '../../shared/models/postRequest.model';

import {DraftRequest} from '../../shared/models/draftRequest.model';
import {Draft} from '../../shared/models/draft.model';
import {AuthService} from '../../shared/services/auth.service';

@Component({
  selector: 'app-edit-draft',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
],
  templateUrl: './edit-draft.component.html',
  styleUrl: './edit-draft.component.css'
})
export class EditDraftComponent implements OnInit{
  router : Router = inject(Router);
  fb: FormBuilder = inject(FormBuilder);
  authService : AuthService = inject(AuthService);

  draftId! : number;
  draft! : Draft;

  editDraftForm = this.fb.group({
    title: ['', Validators.required],
    content: ['', Validators.required]
  });

  constructor(
    private draftService: PostService,
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
    this.draftService.getDraftDetails(id).subscribe({
      next: draft => {
        this.draft = draft;

        this.editDraftForm = this.fb.group({
          title: [this.draft.title, Validators.required],
          content: [this.draft.content, Validators.required]
        })
      },
      error: error => console.log(error),
    });
  }

  saveChanges() {
    let title : string = this.editDraftForm.get('title')!.value!;
    let content : string = this.editDraftForm.get('content')!.value!;
    let author : string = this.draft.author;

    let draftRequest: DraftRequest = new DraftRequest(title, content, author);

    this.draftService.saveEditsToDraft(this.draftId, draftRequest).subscribe({
      next: draft => {
        console.log(draft);
        this.router.navigate([`editor/drafts/${this.draftId}`]);
      },
      error: error => console.log(error)
    });
  }

  return() {
    this.router.navigate([`editor/drafts/${this.draftId}`]);
  }
}
