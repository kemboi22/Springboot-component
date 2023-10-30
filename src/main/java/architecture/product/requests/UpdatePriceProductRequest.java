package architecture.product.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatePriceProductRequest {
    @NotNull
    private UUID id;

    @Min(0L)
    private BigDecimal price;
}
