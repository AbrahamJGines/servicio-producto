package producto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import producto.entity.Product;
import producto.exception.ProductNotFoundException;
import producto.repository.ProductRepository;
import producto.service.ProductService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductById_Success() {
        Product product = new Product(1L, "Laptop", 1500.0, 10);
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));

        Mono<Product> result = productService.getProductById(1L);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop"))
                .verifyComplete();
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Product> result = productService.getProductById(1L);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();
    }

}
