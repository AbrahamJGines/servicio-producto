package producto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import producto.entity.Product;
import producto.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<Product> getAll() {
        return productService.getAllProducts()
                .onErrorResume(e -> Flux.error(new RuntimeException("No Existen productos: " + e.getMessage())));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al obtener el producto: " + e.getMessage())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Product>> create(@RequestBody Product product) {
        return productService.createProduct(product)
                .doOnSuccess(response -> System.out.println("Orden creada Correctamente con id: " + response.getId()))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al crear el producto: " + e.getMessage())));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return productService.deleteProduct(id)
                .doOnSuccess(response -> System.out.println("Producto eliminado con ID: " + id ))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al eliminar el producto: " + e.getMessage())));
    }

}
