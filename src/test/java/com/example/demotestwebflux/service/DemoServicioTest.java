package com.example.demotestwebflux.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class DemoServicioTest {
    @Autowired
    private DemoServicio demoServicio;

    @Test
    void testMono() {
        Mono<String> uno = this.demoServicio.buscarUno();
        StepVerifier.create(uno)
                .expectNext("Pedro")
                .verifyComplete();
    }

    @Test
    void testVarios() {
        Flux<String> uno = this.demoServicio.buscarTodos();
        StepVerifier.create(uno)
                .expectNext("Pedro")
                .expectNext("Maria")
                .expectNext("Jesus")
                .expectNext("Carmen")
                .verifyComplete();
    }

    @Test
    void testVariosLento() {
        Flux<String> uno = this.demoServicio.buscarTodosLento();
        StepVerifier.create(uno)
                .expectNext("Pedro")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("Maria")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("Jesus")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("Carmen")
                .thenAwait(Duration.ofSeconds(1)).verifyComplete();
    }

    @Test
    void testTodosFiltro() {
        Flux<String> source = this.demoServicio.buscarTodosFiltro();
        StepVerifier
                .create(source)
                .expectNextCount(4)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Mensaje de Error")
                ).verify();
    }

    @Test
    void testPersonalizado() {
        Flux<Integer> source = this.demoServicio.personalizado();
        StepVerifier.create(source)
                .expectNext(2)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDropped(4)
                .tookLessThan(Duration.ofMillis(1050));
    }

}