package br.com.manafood.manafoodorder.bdd.hooks

import br.com.manafood.manafoodorder.bdd.context.TestContext
import br.com.manafood.manafoodorder.bdd.support.ProductMockServer
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import io.cucumber.java.After
import io.cucumber.java.Before
import org.springframework.beans.factory.annotation.Autowired

class CucumberHooks {

    @Autowired
    private lateinit var testContext: TestContext

    @Autowired
    private lateinit var orderRepository: OrderJpaRepository

    @Autowired
    private lateinit var productMockServer: ProductMockServer

    @Before
    fun setUp() {
        testContext.reset()
        productMockServer.reset()
    }

    @After
    fun tearDown() {
        try {
            orderRepository.deleteAll()
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
        testContext.reset()
        productMockServer.reset()
    }
}

