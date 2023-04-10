import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.entities.OrderEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.OrderGenerator.randomOrder;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {
    private final List<String> color;
    public CreateOrderParameterizedTest(String[] color) {
        this.color = Arrays.asList(color);
    }
    @Parameterized.Parameters
    public static String[][][] getData() {
        return new String[][][] {
                {{"BLACK"}},
                {{"BLACK","GREY"}},
                {{}}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void createNewOrder() {
        OrderEntity orderEntity = randomOrder(color);
        given()
                .header("Content-type", "application/json")
                .body(orderEntity)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .assertThat().body("track", notNullValue());
    }
}
