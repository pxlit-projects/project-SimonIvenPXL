import {Component, inject, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Post} from '../../shared/models/post.model';
import {Router} from '@angular/router';
import {PostService} from '../../shared/services/post.service';
import {Draft} from '../../shared/models/draft.model';

@Component({
  selector: 'app-draft-overview',
  standalone: true,
  imports: [],
  templateUrl: './draft-overview.component.html',
  styleUrl: './draft-overview.component.css'
})
export class DraftOverviewComponent implements OnInit{
  router : Router = inject(Router);

  draftsObservable: Observable<Draft[]> ;
  drafts: Draft[] = [];

  constructor(
    private postService : PostService,
  ) {
    this.draftsObservable = this.getDrafts();
  }

  ngOnInit() {
    this.draftsObservable.subscribe(drafts => {
      this.drafts = drafts;
    });
  }

  getDrafts() : Observable<Draft[]>  {
    return this.postService.getDrafts();
  }

  getDraftDetails(id : number) {
    this.router.navigate([`editor/drafts/${id}`]);
  }

  backToPosts() {
    this.router.navigate(['editor/posts']);
  }
}
