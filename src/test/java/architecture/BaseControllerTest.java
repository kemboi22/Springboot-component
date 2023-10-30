package architecture;

import architecture.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.lifecycle.Startable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
    @ServiceConnection
    static final Startable container = new MongoDBContainer("mongo");

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void beforeEach() {
        mongoTemplate.getDb().drop();
    }

    private ResultActions perform(HttpMethod method, String uri, Object body) throws Exception {
        final var builder = MockMvcRequestBuilders.request(method, uri);

        builder.header("Authorization", "Bearer " + jwtService.create(Data.user));

        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(body));

        final var result = mockMvc.perform(builder);

        System.out.printf(" Uri: %s%n Method: %s%n Request: %s%n Response: %s%n%n", uri, method, body, result.andReturn().getResponse().getContentAsString());

        return result;
    }

    public ResultActions get(String uri) throws Exception {
        return perform(HttpMethod.GET, uri, null);
    }

    public ResultActions post(String uri, Object body) throws Exception {
        return perform(HttpMethod.POST, uri, body);
    }

    public ResultActions put(String uri, Object body) throws Exception {
        return perform(HttpMethod.PUT, uri, body);
    }

    public ResultActions patch(String uri, Object... args) throws Exception {
        return perform(HttpMethod.PATCH, String.format(uri, args), null);
    }

    public ResultActions delete(String uri) throws Exception {
        return perform(HttpMethod.DELETE, uri, null);
    }
}
