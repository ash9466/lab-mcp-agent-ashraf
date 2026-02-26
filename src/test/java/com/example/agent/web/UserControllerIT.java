package com.example.agent.web;

import com.example.agent.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateThenGetUser() {
        User created =
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/users")
                                .queryParam("name", "Alice")
                                .queryParam("email", "alice@example.com")
                                .build())
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(User.class)
                        .returnResult()
                        .getResponseBody();

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotBlank();
        assertThat(created.name()).isEqualTo("Alice");
        assertThat(created.email()).isEqualTo("alice@example.com");

        webTestClient.get()
                .uri("/api/users/{id}", created.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(fetched -> {
                    assertThat(fetched).isNotNull();
                    assertThat(fetched.id()).isEqualTo(created.id());
                    assertThat(fetched.name()).isEqualTo("Alice");
                    assertThat(fetched.email()).isEqualTo("alice@example.com");
                });
    }
}

