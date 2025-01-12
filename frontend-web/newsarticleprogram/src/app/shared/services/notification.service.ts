import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {WebSocketService} from './websocket.service';
import {Notification} from '../models/notification.model';
import {filter} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    private webSocketService: WebSocketService,
    private snackBar: MatSnackBar
  ) {
  }

  showNotification(notification: Notification): void {
    const message = notification.message.replace(/\n/g, '<br>');

    this.snackBar.open(notification.message);
  }

  connectWebSocket(): void {
    this.webSocketService.connected$.pipe(
      filter(connected => connected) //kijken of connected
    ).subscribe(() => {
      this.webSocketService.subscribeToUserNotifications() //subscriben
        .subscribe(notification => {
          this.showNotification(notification); //notification tonen indien via rabbitmq notification
        });
    });
  }
}
