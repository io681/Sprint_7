import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterCreateOrder;
import ru.praktikum_services.qa_scooter.models.entities.OrderEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.OrderGenerator.randomOrder;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Получить заказ по его номеру")
public class OrderByTrackTest {
    private int trackNumber;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        //создание заказа для получения track
        OrderEntity orderEntity = randomOrder(Arrays.asList("GREY"));
        ResponseBodyAfterCreateOrder responseBodyAfterCreateOrder = given()
                .header("Content-type", "application/json")
                .body(orderEntity)
                .when()
                .post("/api/v1/orders")
                .body().as(ResponseBodyAfterCreateOrder.class);
        trackNumber = responseBodyAfterCreateOrder.getTrack();
    }
    @Test
    @DisplayName("Проверка получения заказа по его track-номеру")
    public void getOrderByIdSuccessTest (){
        given()
                .header("Content-type", "application/json")
                .queryParam("t", trackNumber)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200)
                .assertThat().body("order.id", notNullValue());
    }

    @Test
    @DisplayName("Проверка получения заказа без track-номера")
    public void getOrderByIdWithoutTrackIdTest (){
        String emptyTrackNumber = "";
        given()
                .header("Content-type", "application/json")
                .queryParam("t", emptyTrackNumber)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для поиска"));
    }
    @Test
    @DisplayName("Проверка получения заказа с несуществующим track-номером")
    public void getOrderByIncorrectIdTest (){
        String incorrectTrackNumber = "000000";
        given()
                .header("Content-type", "application/json")
                .queryParam("t", incorrectTrackNumber)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(404)
                .assertThat().body("message",equalTo("Заказ не найден"));
    }
}
