# Architecture

![eShopOnContainers Architecture](architecture.png)

## Communicatie
De app bestaat uit 3 microservices die elk onderling synchroon met elkaar communiceren via OpenFeign,
 alsook een extra NotificationService die via een message bus asynchroon zal communiceren met de Review Service voor notificaties te sturen.