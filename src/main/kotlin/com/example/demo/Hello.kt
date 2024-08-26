package com.example.demo

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(private val service: HelloService) {
    @PutMapping("/hello/{id}")
    suspend fun update(@PathVariable("id") id: Long): Unit = service.update(id)

    @GetMapping("/hello/{id}")
    suspend fun find(@PathVariable("id") id: Long): HelloEntity? = service.find(id)
}

@Service
class HelloService(private val helloRepository: HelloRepository) {
    @Transactional
    suspend fun update(id: Long) {
        helloRepository.updateMessage(id, "Hello, Spring Boot!")
    }

    @Transactional
    suspend fun find(id: Long): HelloEntity? = helloRepository.suspendFindById(id)
}

@Entity
class HelloEntity(@Id var id: Long? = null, var message: String = "Hello")

@Repository
interface HelloRepository : ListCrudRepository<HelloEntity, Long> {
    @Modifying
    @Query("update HelloEntity e set e.message = :message where e.id = :id")
    suspend fun updateMessage(id: Long, message: String): Int

    @Query("select e from HelloEntity e where e.id = :id")
    suspend fun suspendFindById(id: Long): HelloEntity?
}
