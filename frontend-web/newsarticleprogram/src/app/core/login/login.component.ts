import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../shared/services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule
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
