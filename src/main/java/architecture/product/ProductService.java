package architecture.product;

import architecture.product.requests.AddProductRequest;
import architecture.product.requests.GetProductRequest;
import architecture.product.requests.UpdatePriceProductRequest;
import architecture.product.requests.UpdateProductRequest;
import architecture.shared.services.MapperService;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MapperService mapperService;
    private final ProductRepository productRepository;

    public Page<ProductModel> get(final GetProductRequest request) {
        final var entities = productRepository.findAll(request.getExample(Product.class), request.getPageable());

        if (entities.isEmpty()) throw new NoSuchElementException();

        return mapperService.map(entities, ProductModel.class);
    }

    public ProductModel get(final UUID id) {
        final var entity = productRepository.findById(id).orElseThrow();

        return mapperService.map(entity, ProductModel.class);
    }

    public UUID add(final AddProductRequest request) {
        var entity = mapperService.map(request, Product.class);

        entity.setId(UUID.randomUUID());

        entity = productRepository.insert(entity);

        return entity.getId();
    }

    public void update(final UpdateProductRequest request) {
        final var entity = mapperService.map(request, Product.class);

        productRepository.save(entity);
    }

    public void update(final UpdatePriceProductRequest request) {
        final var entity = productRepository.findById(request.getId()).orElseThrow();

        BeanUtils.copyProperties(request, entity);

        productRepository.save(entity);
    }

    public void delete(final UUID id) {
        productRepository.deleteById(id);
    }
}
