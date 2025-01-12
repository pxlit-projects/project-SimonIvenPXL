export class Notification {
  sender: string;
  message: string;
  receiver: string;

  constructor(sender: string, message: string, receiver: string) {
    this.sender = sender;
    this.message = message;
    this.receiver = receiver;
  }
}
