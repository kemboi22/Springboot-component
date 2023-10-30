package architecture.product;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductModel {
    private UUID id;
    private String description;
    private BigDecimal price;
}
