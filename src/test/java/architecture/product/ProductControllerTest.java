package architecture.product;

import architecture.BaseControllerTest;
import architecture.Data;
import architecture.product.requests.AddProductRequest;
import architecture.product.requests.UpdateProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.BeanUtils;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends BaseControllerTest {
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "?page=-1 | Page: must be greater than or equal to 0.",
        "?page=9999999999 | Page: must be a valid value.",
        "?page=X | Page: must be a valid value.",
        "?size=0 | Size: must be greater than 0.",
        "?size=9999999999 | Size: must be a valid value.",
        "?size=X | Size: must be a valid value.",
        "?sort=invalid | Sort: must match [id, description].",
        "?direction=invalid | Direction: must be a valid value.",
        "/1 | Id: must be a valid value.",
        "/X | Id: must be a valid value."
    })
    void testGetShouldReturnBadRequest(String uri, String message) throws Exception {
        get("/products" + uri).andExpectAll(status().isBadRequest(), jsonPath("$").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "?description=invalid",
        "/" + Data.idInexistent
    })
    void testGetShouldReturnNotFound(String uri) throws Exception {
        mongoTemplate.save(Data.product);

        get("/products" + uri).andExpectAll(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "page=0",
        "size=1",
        "sort=id",
        "sort=description",
        "direction=ASC",
        "direction=DESC",
        "description=Product"
    })
    void testGetShouldReturnOk(String uri) throws Exception {
        mongoTemplate.save(Data.product);

        get("/products?" + uri).andExpectAll(
            status().isOk(),
            jsonPath("$.content").isArray(),
            jsonPath("$.content.length()").value(1),
            jsonPath("$.empty").value(false),
            jsonPath("$.first").value(true),
            jsonPath("$.last").value(true),
            jsonPath("$.numberOfElements").value(1),
            jsonPath("$.totalElements").value(1),
            jsonPath("$.number").value(0),
            jsonPath("$.totalPages").value(1),
            jsonPath("$.sort.empty").value(false),
            jsonPath("$.sort.sorted").value(true),
            jsonPath("$.sort.unsorted").value(false),
            jsonPath("$.content[0].id").value(Data.product.getId().toString()),
            jsonPath("$.content[0].description").value(Data.product.getDescription()),
            jsonPath("$.content[0].price").value(Data.product.getPrice())
        );
    }

    @Test
    void testGetShouldReturnOk() throws Exception {
        mongoTemplate.save(Data.product);

        get("/products/" + Data.product.getId()).andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(Data.product.getId().toString()),
            jsonPath("$.description").value(Data.product.getDescription()),
            jsonPath("$.price").value(Data.product.getPrice())
        );
    }

    @Test
    void testPostShouldReturnCreated() throws Exception {
        final var request = new AddProductRequest();

        BeanUtils.copyProperties(Data.product, request);

        post("/products", request).andExpectAll(status().isCreated());
    }

    @Test
    void testPutShouldReturnOk() throws Exception {
        mongoTemplate.save(Data.product);

        final var request = new UpdateProductRequest();

        BeanUtils.copyProperties(Data.product, request);

        put("/products/" + Data.product.getId(), request).andExpectAll(status().isOk());

        get("/products/" + Data.product.getId()).andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(request.getId().toString()),
            jsonPath("$.description").value(request.getDescription()),
            jsonPath("$.price").value(request.getPrice())
        );
    }

    @Test
    void testPatchShouldReturnNotFound() throws Exception {
        patch("/products/%s/price/%s", Data.product.getId(), Data.productUpdate.getPrice()).andExpectAll(status().isNotFound());
    }

    @Test
    void testPatchShouldReturnOk() throws Exception {
        mongoTemplate.save(Data.product);

        patch("/products/%s/price/%s", Data.product.getId(), Data.productUpdate.getPrice()).andExpectAll(status().isOk());

        get("/products/" + Data.product.getId()).andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(Data.product.getId().toString()),
            jsonPath("$.description").value(Data.product.getDescription()),
            jsonPath("$.price").value(Data.productUpdate.getPrice())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        Data.id,
        Data.idInexistent
    })
    void testDeleteShouldReturnOk(String id) throws Exception {
        mongoTemplate.save(Data.product);

        delete("/products/" + id).andExpectAll(status().isOk());
    }
}
