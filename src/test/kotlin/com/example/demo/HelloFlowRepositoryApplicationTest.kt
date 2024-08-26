package com.example.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@EnableJpaRepositories(considerNestedRepositories = true)
@Sql(statements = ["INSERT INTO hello_entity (id, message) VALUES (1, 'message');"])
class HelloFlowRepositoryApplicationTest(@Autowired private val repository: HelloFlowRepository) {
    /**
     * This test will fail because the repository is not reactive.
     */
    interface HelloFlowRepository : JpaRepository<HelloEntity, Long> {
        @Query("select e from HelloEntity e")
        fun findAllFlow(): Flow<HelloEntity>
    }

    @Test
    fun `Reactive Repositories are not supported by JPA`() = runTest {
        val result = repository.findAllFlow().toList()

        assert(result.size == 1)
    }
}
