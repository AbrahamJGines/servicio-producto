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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_Success() {

        Product p1 = new Product(1L, "Laptop", 1500.0, 10);
        Product p2 = new Product(2L, "Mouse", 25.0, 50);
        when(productRepository.findAll()).thenReturn(Flux.just(p1, p2));

        Flux<Product> result = productService.getAllProducts();

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop"))
                .expectNextMatches(p -> p.getName().equals("Mouse"))
                .verifyComplete();
    }

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

    @Test
    void createProduct_Success() {
        Product newProduct = new Product(null, "Teclado", 50.0, 20);
        Product savedProduct = new Product(1L, "Teclado", 50.0, 20);

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        Mono<Product> result = productService.createProduct(newProduct);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId() != null && p.getName().equals("Teclado"))
                .verifyComplete();
    }

    @Test
    void createProduct_InvalidStock_ThrowsException() {
        Product invalidProduct = new Product(null, "Teclado", 50.0, -5);

        Mono<Product> result = productService.createProduct(invalidProduct);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void deleteProduct_Success() {
        Product existingProduct = new Product(1L, "Monitor", 300.0, 15);
        when(productRepository.findById(1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.delete(existingProduct)).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProduct(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProduct(1L);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();
    }

}
