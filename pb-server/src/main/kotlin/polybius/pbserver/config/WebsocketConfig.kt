package polybius.pbserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@Configuration
@EnableWebSocketMessageBroker
class WebsocketConfig : AbstractWebSocketMessageBrokerConfigurer() {
    override fun configureMessageBroker(registry: MessageBrokerRegistry?) {
        registry?.enableSimpleBroker("/topic")
        registry?.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        registry?.addEndpoint("/ws/channel")?.setAllowedOrigins("*")
    }
}