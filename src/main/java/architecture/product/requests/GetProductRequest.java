package architecture.product.requests;

import architecture.shared.requests.PageableRequest;
import architecture.shared.validators.AllowedValues;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllowedValues(field = "sort", values = { "id", "description" })
public final class GetProductRequest extends PageableRequest {
    private String description;

    public GetProductRequest() {
        setSort("description");
    }
}
