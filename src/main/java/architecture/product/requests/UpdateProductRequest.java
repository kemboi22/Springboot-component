package architecture.product.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @JsonIgnore
    private UUID id;

    @NotBlank
    private String description;

    @Min(0L)
    private BigDecimal price;
}
