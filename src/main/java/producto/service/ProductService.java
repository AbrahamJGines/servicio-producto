package producto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import producto.entity.Product;
import producto.exception.ProductNotFoundException;
import producto.repository.ProductRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)));
    }

    public Mono<Product> createProduct(Product product) {
        return Mono.just(product)
                .filter(p -> p.getPrice() > 0 && p.getStock() >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Precio o stock inválidos")))
                .flatMap(productRepository::save);
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado para eliminar")))
                .flatMap(productRepository::delete);
    }

}
