import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterCreateOrder;
import ru.praktikum_services.qa_scooter.models.bodies.orderByTrack.Order;
import ru.praktikum_services.qa_scooter.models.bodies.orderByTrack.ResponseBodyAfterGetOrderByTrackEntity;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterLogin;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import ru.praktikum_services.qa_scooter.models.entities.OrderEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;
import static ru.praktikum_services.qa_scooter.models.entities.OrderGenerator.randomOrder;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest {
    private String courierId;
    private String orderId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        //создание курьера и авторизация для получения id курьера
        Courier courier = randomCourier();
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok",equalTo(true));

        //авторизация курьера для получения id курьера
        RequestBodyForLogin requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
        ResponseBodyAfterLogin responseBodyAfterLogin = given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .body().as(ResponseBodyAfterLogin.class);
        //получения id курьера
        courierId = responseBodyAfterLogin.getId();

        //создать заказ
        OrderEntity orderEntity = randomOrder(Arrays.asList("GREY"));
        ResponseBodyAfterCreateOrder responseBodyAfterCreateOrder = given()
                .header("Content-type", "application/json")
                .body(orderEntity)
                .when()
                .post("/api/v1/orders")
                .body().as(ResponseBodyAfterCreateOrder.class);

        //запросить заказ по номеру track получить id заказа по track заказа
        ResponseBodyAfterGetOrderByTrackEntity responseBodyAfterGetOrderByTrack = given()
                .header("Content-type", "application/json")
                .queryParam("t", responseBodyAfterCreateOrder.getTrack())
                .when()
                .get("/api/v1/orders/track")
                .body().as(ResponseBodyAfterGetOrderByTrackEntity.class);

        //получить id заказа по track заказа
        Order orderObject = responseBodyAfterGetOrderByTrack.getOrder();
        orderId = orderObject.getId();
    }
    @Test
    @DisplayName("Проверка успешного принятия заказа")
    public void successAcceptOrderTest (){
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/"+ orderId)
                .then()
                .statusCode(200)
                .assertThat().body("ok",equalTo(true));
    }
    @Test
    @DisplayName("Проверка запроса без id курьера")
    public void acceptOrderWithoutCourierIdTest (){
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", "")
                .when()
                .put("/api/v1/orders/accept/"+ orderId)
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для поиска"));
    }
    @Test
    @DisplayName("Проверка запроса с неверным id курьера")
    public void acceptOrderWithIncorrectCourierIdTest (){
        String IncorrectCourierId = "999999";
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", IncorrectCourierId)
                .when()
                .put("/api/v1/orders/accept/"+ orderId)
                .then()
                .statusCode(404)
                .assertThat().body("message",equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Проверка запроса без id заказа")
    @Description("Тест будет падать, т.к. сервер возвращает другую ошибку 404 и  другой message (не по спецификации)")
    public void acceptOrderWithoutOrderIdTest (){
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/")
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для поиска"));
    }
    @Test
    @DisplayName("Проверка запроса c неверным id заказа")
    public void acceptOrderWithIncorrectOrderIdTest (){
        String IncorrectOrderId = "000000";
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/"+ IncorrectOrderId)
                .then()
                .statusCode(404)
                .assertThat().body("message",equalTo("Заказа с таким id не существует"));
    }
    @After
    public void cleanTestData(){

        //удаление курьера
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/"+ courierId);
    }

}
