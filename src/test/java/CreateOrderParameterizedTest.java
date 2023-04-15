import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.OrderApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа")
public class CreateOrderParameterizedTest {
    private final List<String> color;
    private OrderApi orderApi;
    public CreateOrderParameterizedTest(String[] color) {
        this.color = Arrays.asList(color);
    }
    @Parameterized.Parameters
    public static String[][][] getData() {
        return new String[][][] {
                {{"BLACK"}},
                {{"GREY"}},
                {{"BLACK","GREY"}},
                {{}}
        };
    }
    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }
    @Test
    @DisplayName("Создание заказа, параметризированный по цвету самоката")
    public void createNewOrder() {
        ValidatableResponse response = orderApi.createOrder(color);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_CREATED, response.extract().statusCode());
        assertNotNull("Пустой track в респонзе после создания заказа", response.extract().path("track"));
    }
}
