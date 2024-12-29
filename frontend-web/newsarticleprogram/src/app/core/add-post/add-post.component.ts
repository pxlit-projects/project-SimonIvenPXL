import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-add-post',
  standalone: true,
  imports: [],
  templateUrl: './add-post.component.html',
  styleUrl: './add-post.component.css'
})
export class AddPostComponent implements OnInit{
  router : Router = inject(Router);

  constructor(
    private location : Location
  ) {
  }

  ngOnInit() {

  }

  onReturn() {
    this.location.back();
  }
}
