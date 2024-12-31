import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  login(username: string, role: string): void {
    localStorage.setItem('user', username);
    localStorage.setItem('role', role);
    console.log('User logged in:', { username, role });
  }

  getUser(): { username: string | null; role: string | null } {
    const username = localStorage.getItem('user');
    const role = localStorage.getItem('role');
    return { username, role };
  }

  logout(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('role');
    console.log('User logged out');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('user'); // converts to boolean
  }

  isAdmin() : boolean {
    const role = localStorage.getItem('role');
    return role === 'editor';
  }
}
