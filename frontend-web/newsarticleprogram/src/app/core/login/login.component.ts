import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../shared/services/auth.service';
import {Router} from '@angular/router';

import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatFormFieldModule, MatInputModule, MatSelectModule, FormsModule, MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  role: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    if (this.username && this.role) {
      this.authService.login(this.username, this.role);
      this.router.navigate(['posts']); // Redirect after login
    } else {
      alert('Please fill in all fields.');
    }
  }
}
