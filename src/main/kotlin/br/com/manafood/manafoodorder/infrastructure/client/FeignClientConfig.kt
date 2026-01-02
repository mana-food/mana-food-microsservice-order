package br.com.manafood.manafoodorder.infrastructure.client

import feign.Logger
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Feign Client Configuration for Product Service.
 * Configures logging, error handling, and request interceptors.
 */
@Configuration
class FeignClientConfig {

    /**
     * Configure Feign logging level
     */
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    /**
     * Custom error decoder for handling Feign errors
     */
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return ErrorDecoder.Default()
    }

    /**
     * Request interceptor to add headers to all requests
     */
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header("Content-Type", "application/json")
            template.header("Accept", "application/json")
        }
    }
}

