import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const user = this.authService.getUser().username;
    const role = this.authService.getUser().role;

    if (user && role) {
      request = request.clone({
        setHeaders: {
          User: user,
          Role: role,
        },
      });
    }
    console.log('Request Headers:', {
      User: request.headers.get('User'),
      Role: request.headers.get('Role'),
    });

    return next.handle(request);
  }
}
