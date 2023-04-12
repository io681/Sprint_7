import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.OrderApi;
import ru.praktikum_services.qa_scooter.models.bodies.listOrders.Orders;
import ru.praktikum_services.qa_scooter.models.bodies.listOrders.ResponseBodyAfterGetListOrders;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@DisplayName("Список заказов")
public class ListOrderTest {
    private OrderApi orderApi;
    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }
    @Test
    @DisplayName("Проверка запроса получения списка заказов")
    public void getListOrderTest (){
        ValidatableResponse response = orderApi.getListOrder();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertNotNull("Пустой orders в респонзе", response.extract().path("orders"));
        ResponseBodyAfterGetListOrders responseBodyAfterGetListOrders = orderApi.getResponseBodyAfterGetListOrders();
        assertThat(responseBodyAfterGetListOrders.getOrders().get(0), instanceOf(Orders.class));
    }
}
