package com.monsalon.monSalonBackend.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker.
        // Messages whose destination starts with "/topic" or "/queue" will be routed to the broker.
        config.enableSimpleBroker("/topic", "/queue");

        // Set the application destination prefix.
        // Messages whose destination starts with "/app" will be routed to @MessageMapping methods.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the "/ws" endpoint, enabling SockJS fallback options.
        // SockJS is used for browsers that don't support native WebSockets.
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:*", "http://yourfrontenddomain.com") // IMPORTANT: Configure allowed origins for production
                .withSockJS();
    }
}
