package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Logger logger;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification("Test message", "senderUser", "receiverUser");
    }


    @Test
    void receiveNotification_ShouldSendNotificationThroughSocket() {
        NotificationService spyNotificationService = Mockito.spy(notificationService);
        // When
        spyNotificationService.receiveNotification(notification);

        // Verify that sendNotificationThroughSocket was called
        verify(spyNotificationService).sendNotificationThroughSocket(notification);
    }

    @Test
    void sendNotificationThroughSocket_ShouldSendNotificationThroughSocket() {
        // When
        notificationService.sendNotificationThroughSocket(notification);

        // Verify that messagingTemplate.convertAndSendToUser was called with the correct parameters
        ArgumentCaptor<String> receiverCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        verify(messagingTemplate).convertAndSendToUser(receiverCaptor.capture(), destinationCaptor.capture(), messageCaptor.capture());

        // Verify the values of the captors
        assertEquals("receiverUser", receiverCaptor.getValue());
        assertEquals("/notifications", destinationCaptor.getValue());
        assertEquals(notification, messageCaptor.getValue());
    }

    @Test
    void sendNotificationThroughSocket_ShouldHandleExceptionGracefully() {
        // Simulate an exception being thrown
        Mockito.doThrow(new RuntimeException("Test exception")).when(messagingTemplate).convertAndSendToUser(Mockito.any(), Mockito.any(), Mockito.any());

        // When
        assertDoesNotThrow(() -> notificationService.sendNotificationThroughSocket(notification));

        // Verify that the exception was caught and logged
        verify(logger, never()).error(Mockito.anyString(), Optional.ofNullable(Mockito.any()));
    }
}
