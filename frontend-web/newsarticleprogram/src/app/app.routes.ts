import { Routes } from '@angular/router';
import {PostOverviewComponent} from './core/post-overview/post-overview.component';
import {AddPostComponent} from './core/add-post/add-post.component';
import {EditPostComponent} from './core/edit-post/edit-post.component';
import {PostDetailsComponent} from './core/post-details/post-details.component';
import {DraftDetailsComponent} from './core/draft-details/draft-details.component';
import {EditDraftComponent} from './core/edit-draft/edit-draft.component';
import {DraftOverviewComponent} from './core/draft-overview/draft-overview.component';
import {LoginComponent} from './core/login/login.component';
import {AuthGuard} from './shared/guard/auth-guard';
import {AddReviewComponent} from './core/add-review/add-review.component';

export const routes: Routes = [
  {path : "login", component : LoginComponent},

  // EDITOR ROUTES
  {path : 'editor/posts/add', component : AddPostComponent, canActivate: [AuthGuard]},
  {path : 'editor/posts/:id/edit', component : EditPostComponent, canActivate: [AuthGuard]},

  {path : 'editor/drafts', component : DraftOverviewComponent, canActivate: [AuthGuard]},
  {path : 'editor/drafts/:id', component : DraftDetailsComponent, canActivate: [AuthGuard]},
  {path : 'editor/drafts/:id/edit', component : EditDraftComponent, canActivate: [AuthGuard]},

  {path : "editor/posts/:id/review", component: AddReviewComponent, canActivate: [AuthGuard]},

  // USER ROUTES OR COMMON ROUTES
  {path : 'posts', component : PostOverviewComponent},
  {path : 'posts/:id', component : PostDetailsComponent},

  {path : '**', redirectTo : 'posts'}
];
