package architecture.product.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AddProductRequest {
    @NotBlank
    private String description;

    @Min(0L)
    private BigDecimal price;
}
