package com.example.demo

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@Sql(statements = ["INSERT INTO hello_entity (id, message) VALUES (1, 'message');"])
class HelloRepositoryPersistenceTest(@Autowired private val repository: HelloRepository) {
    @Test
    fun `Failed to convert from type error`() = runTest {
        val result = repository.suspendFindById(1L)

        assert(result != null)
        assert(result!!.id == 1L)
        assert(result.message == "message")
    }

    @Test
    fun `Reactive source object must not be null error`() = runTest {
        val result = repository.suspendFindById(2L)

        assert(result == null)
    }

    @Test
    fun `Failed to convert from type (java_lang_Integer) to type (reactor_core_publisher_Flux)`() = runTest {
        val result = repository.updateMessage(1L, "Hello, Spring Boot!")

        assert(result == 1)
    }
}
