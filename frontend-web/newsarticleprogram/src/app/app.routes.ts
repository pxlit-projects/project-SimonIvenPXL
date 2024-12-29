import { Routes } from '@angular/router';
import {PostOverviewComponent} from './core/post-overview/post-overview.component';
import {AddPostComponent} from './core/add-post/add-post.component';

export const routes: Routes = [
  {path : 'editor/posts', component : PostOverviewComponent},
  {path : 'editor/posts/add', component : AddPostComponent},


  {path : '**', redirectTo : 'editor/posts'}
];
