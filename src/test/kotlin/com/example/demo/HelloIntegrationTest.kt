package com.example.demo

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS
import kotlin.test.Test

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Sql(statements = ["INSERT INTO hello_entity (id, message) VALUES (1, 'message');"], executionPhase = BEFORE_TEST_CLASS)
class HelloIntegrationTest(@Autowired private val service: HelloService) {
    @Test
    @Order(1)
    fun `Failed to convert from type error`() = runTest {
        val result = service.find(1L)

        assert(result != null)
        assert(result!!.id == 1L)
        assert(result.message == "message")
    }

    @Test
    @Order(2)
    fun `Reactive source object must not be null error`() = runTest {
        val result = service.find(2L)

        assert(result == null)
    }

    @Test
    @Order(3)
    fun `@Transactional is set on service but not propagated to repository invocation`() = runTest {
        service.update(1L)

        val result = service.find(1L)
        assert(result != null)
        assert(result!!.id == 1L)
        assert(result.message == "Hello, Spring Boot!")
    }
}
