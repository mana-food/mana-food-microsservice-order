package br.com.manafood.manafoodorder.infrastructure.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

/**
 * General Bean Configuration for the application.
 * Enables Feign Clients and other cross-cutting concerns.
 */
@Configuration
@EnableFeignClients(basePackages = ["br.com.manafood.manafoodorder.infrastructure.client"])
class BeanConfig {

    // Future beans can be configured here
    // Example: ObjectMapper, RestTemplate, etc.
}

