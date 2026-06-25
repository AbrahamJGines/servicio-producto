package producto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import producto.controller.ProductController;
import producto.entity.Product;
import producto.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllProducts() {
        Product product = new Product(1L, "Mouse", 25.0, 50);
        when(productService.getAllProducts()).thenReturn(Flux.just(product));

        webTestClient.get().uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(1)
                .contains(product);
    }

    @Test
    void getProductById_Success_ReturnsOk() {
        Product p = new Product(1L, "Monitor", 200.0, 10);
        when(productService.getProductById(1L)).thenReturn(Mono.just(p));

        webTestClient.get().uri("/api/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Monitor");
    }

    @Test
    void createProduct_InvalidData_ReturnsError() {
        Product invalidProduct = new Product(null, "Cable", -10.0, 5);

        when(productService.createProduct(any(Product.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Precio o stock invalidos")));

        webTestClient.post().uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidProduct)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void deleteProduct_Success_ReturnsNoContent() {
        when(productService.deleteProduct(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/products/1")
                .exchange()
                .expectStatus().isNoContent();
    }

}
