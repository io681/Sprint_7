import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.listOrders.Orders;
import ru.praktikum_services.qa_scooter.models.bodies.listOrders.ResponseBodyAfterGetListOrders;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ListOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Проверка запроса получения списка заказов")
    public void getListOrderTest (){

        //проверка, что поле orders не пустое
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .assertThat().body("orders", notNullValue());

        //проверка, что в  тело ответа возвращается список заказов
        ResponseBodyAfterGetListOrders responseBodyAfterGetListOrders = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .body().as(ResponseBodyAfterGetListOrders.class);
        assertThat(responseBodyAfterGetListOrders.getOrders().get(0), instanceOf(Orders.class));
    }
}
