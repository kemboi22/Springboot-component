package architecture.user;

import architecture.BaseControllerTest;
import architecture.Data;
import architecture.user.requests.AddUserRequest;
import architecture.user.requests.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.BeanUtils;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "?page=-1 | Page: must be greater than or equal to 0.",
        "?page=9999999999 | Page: must be a valid value.",
        "?page=X | Page: must be a valid value.",
        "?size=0 | Size: must be greater than 0.",
        "?size=9999999999 | Size: must be a valid value.",
        "?size=X | Size: must be a valid value.",
        "?sort=invalid | Sort: must match [id, name].",
        "?direction=invalid | Direction: must be a valid value.",
        "/1 | Id: must be a valid value.",
        "/X | Id: must be a valid value."
    })
    void testGetShouldReturnBadRequest(String uri, String message) throws Exception {
        get("/users" + uri).andExpectAll(status().isBadRequest(), jsonPath("$").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "?name=invalid",
        "/" + Data.idInexistent
    })
    void testGetShouldReturnNotFound(String uri) throws Exception {
        mongoTemplate.save(Data.user);

        get("/users" + uri).andExpectAll(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "page=0",
        "size=1",
        "sort=id",
        "sort=name",
        "direction=ASC",
        "direction=DESC",
        "name=Admin"
    })
    void testGetShouldReturnOk(String uri) throws Exception {
        mongoTemplate.save(Data.user);

        get("/users?" + uri).andExpectAll(
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
            jsonPath("$.content[0].id").value(Data.user.getId().toString()),
            jsonPath("$.content[0].name").value(Data.user.getName()),
            jsonPath("$.content[0].email").value(Data.user.getEmail())
        );
    }

    @Test
    void testGetShouldReturnOk() throws Exception {
        mongoTemplate.save(Data.user);

        get("/users/" + Data.user.getId()).andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(Data.user.getId().toString()),
            jsonPath("$.name").value(Data.user.getName()),
            jsonPath("$.email").value(Data.user.getEmail())
        );
    }

    @Test
    void testPostShouldReturnConflict() throws Exception {
        mongoTemplate.save(Data.user);

        final var request = new AddUserRequest();

        BeanUtils.copyProperties(Data.user, request);

        request.setPassword(Data.password);

        post("/users", request).andExpectAll(status().isConflict(), jsonPath("$").value("Unique fields with existing values."));
    }

    @Test
    void testPostShouldReturnCreated() throws Exception {
        final var request = new AddUserRequest();

        BeanUtils.copyProperties(Data.user, request);

        request.setPassword(Data.password);

        post("/users", request).andExpectAll(status().isCreated());
    }

    @Test
    void testPutShouldReturnConflict() throws Exception {
        mongoTemplate.save(Data.user);

        mongoTemplate.save(Data.userConflict);

        final var request = new UpdateUserRequest();

        BeanUtils.copyProperties(Data.userConflict, request);

        request.setPassword(Data.password);

        put("/users/" + Data.userConflict.getId(), request).andExpectAll(status().isConflict(), jsonPath("$").value("Unique fields with existing values."));
    }

    @Test
    void testPutShouldReturnOk() throws Exception {
        mongoTemplate.save(Data.user);

        final var request = new UpdateUserRequest();

        BeanUtils.copyProperties(Data.userUpdate, request);

        request.setPassword(Data.password);

        put("/users/" + Data.user.getId(), request).andExpectAll(status().isOk());

        get("/users/" + Data.user.getId()).andExpectAll(
            status().isOk(),
            jsonPath("$.id").value(request.getId().toString()),
            jsonPath("$.name").value(request.getName()),
            jsonPath("$.email").value(request.getEmail())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        Data.id,
        Data.idInexistent
    })
    void testDeleteShouldReturnOk(String id) throws Exception {
        mongoTemplate.save(Data.user);

        delete("/users/" + id).andExpectAll(status().isOk());
    }
}
