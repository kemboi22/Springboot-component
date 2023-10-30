package architecture.product;

import architecture.product.requests.AddProductRequest;
import architecture.product.requests.GetProductRequest;
import architecture.product.requests.UpdatePriceProductRequest;
import architecture.product.requests.UpdateProductRequest;
import architecture.shared.swagger.DefaultApiResponses;
import architecture.shared.swagger.GetApiResponses;
import architecture.shared.swagger.PostApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Products")
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get")
    @GetApiResponses
    @GetMapping
    public Page<ProductModel> get(@ParameterObject @ModelAttribute @Valid final GetProductRequest request) {
        return productService.get(request);
    }

    @Operation(summary = "Get")
    @GetApiResponses
    @GetMapping("{id}")
    public ProductModel get(@PathVariable final UUID id) {
        return productService.get(id);
    }

    @Operation(summary = "Add")
    @PostApiResponses
    @PostMapping
    public UUID add(@RequestBody @Valid final AddProductRequest request) {
        return productService.add(request);
    }

    @Operation(summary = "Update")
    @DefaultApiResponses
    @PutMapping("{id}")
    public void update(@PathVariable final UUID id, @RequestBody @Valid final UpdateProductRequest request) {
        request.setId(id);
        productService.update(request);
    }

    @Operation(summary = "Update Price")
    @DefaultApiResponses
    @PatchMapping("{id}/price/{price}")
    public void update(@PathVariable final UUID id, @PathVariable final BigDecimal price) {
        productService.update(new UpdatePriceProductRequest(id, price));
    }

    @Operation(summary = "Delete")
    @DefaultApiResponses
    @DeleteMapping("{id}")
    public void delete(@PathVariable final UUID id) {
        productService.delete(id);
    }
}
