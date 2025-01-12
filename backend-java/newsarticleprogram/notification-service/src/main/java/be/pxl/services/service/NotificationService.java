package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(Notification notification) {
        log.info("Notification sent...");
        log.info("Sending notification: {}", notification.getMessage());
        log.info("Notification received by {}", notification.getSender());
    }

    @RabbitListener(queues = "NotificationQueue")
    public void receiveNotification(Notification notification) {
        log.info("Notification received...");
        log.info("Sending notification: {}", notification.getMessage());
        log.info("Notification received by {}", notification.getSender());
        sendNotificationThroughSocket(notification);
    }

    public void sendNotificationThroughSocket(Notification notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    notification.getReceiver(),
                    "/notifications",
                    notification
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
