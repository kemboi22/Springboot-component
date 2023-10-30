package architecture.user.requests;

import architecture.shared.requests.PageableRequest;
import architecture.shared.validators.AllowedValues;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllowedValues(field = "sort", values = { "id", "name" })
public final class GetUserRequest extends PageableRequest {
    private String name;

    public GetUserRequest() {
        setSort("name");
    }
}
