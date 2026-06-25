package producto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import producto.controller.ProductController;
import producto.entity.Product;
import producto.service.ProductService;
import reactor.core.publisher.Flux;

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
}
