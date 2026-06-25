package producto.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import producto.entity.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}
