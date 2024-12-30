import { Routes } from '@angular/router';
import {PostOverviewComponent} from './core/post-overview/post-overview.component';
import {AddPostComponent} from './core/add-post/add-post.component';
import {EditPostComponent} from './core/edit-post/edit-post.component';
import {PostDetailsComponent} from './core/post-details/post-details.component';
import {DraftDetailsComponent} from './core/draft-details/draft-details.component';
import {EditDraftComponent} from './core/edit-draft/edit-draft.component';
import {DraftOverviewComponent} from './core/draft-overview/draft-overview.component';

export const routes: Routes = [
  {path : 'editor/posts', component : PostOverviewComponent},
  {path : 'editor/posts/add', component : AddPostComponent},
  {path : 'editor/posts/:id', component : PostDetailsComponent},
  {path : 'editor/posts/:id/edit', component : EditPostComponent},

  {path : 'editor/drafts', component : DraftOverviewComponent},
  {path : 'editor/drafts/:id', component : DraftDetailsComponent},
  {path : 'editor/drafts/:id/edit', component : EditDraftComponent},


  {path : '**', redirectTo : 'editor/posts'}
];
