package ru.praktikum_services.qa_scooter.api;

import io.restassured.response.ValidatableResponse;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import ru.praktikum_services.qa_scooter.models.bodies.listOrders.ResponseBodyAfterGetListOrders;
import ru.praktikum_services.qa_scooter.models.bodies.orderByTrack.ResponseBodyAfterGetOrderByTrack;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import ru.praktikum_services.qa_scooter.models.entities.OrderEntity;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;
import static ru.praktikum_services.qa_scooter.models.entities.OrderGenerator.randomOrder;

public class OrderApi extends BaseApi {
    private static final String PATH_ORDER_API = "api/v1/orders/";
    private OrderEntity orderEntity;
    private ResponseBodyAfterGetListOrders responseBodyAfterGetListOrders;
    private ResponseBodyAfterGetOrderByTrack responseBodyAfterGetOrderByTrack;

    public OrderApi (){
        super();
        responseBodyAfterGetListOrders = new ResponseBodyAfterGetListOrders();

    }
    //геттер
    public ResponseBodyAfterGetListOrders getResponseBodyAfterGetListOrders() {
        return responseBodyAfterGetListOrders;
    }

    //создание заказа
    public ValidatableResponse createOrder (List<String> color) {
        this.orderEntity = randomOrder(color);
        return given()
                .header("Content-type", "application/json")
                .body(orderEntity)
                .when()
                .post(PATH_ORDER_API)
                .then();
    }
    //получение списка заказов и десериализация респонза
    public ValidatableResponse getListOrder(){
        this.responseBodyAfterGetListOrders = given()
                .header("Content-type", "application/json")
                .when()
                .get(PATH_ORDER_API)
                .body().as(ResponseBodyAfterGetListOrders.class);
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("PATH_ORDER_API")
                .then();
    }
    // получение заказа по его номеру и десериализация респонза
    public ValidatableResponse getOrderById (int trackNumber){
        responseBodyAfterGetOrderByTrack = given()
                .header("Content-type", "application/json")
                .queryParam("t", trackNumber)
                .when()
                .get(PATH_ORDER_API + "track")
                .body().as(ResponseBodyAfterGetOrderByTrack.class);
        return given()
                .header("Content-type", "application/json")
                .queryParam("t", trackNumber)
                .when()
                .get(PATH_ORDER_API + "track")
                .then();
    }

    public ValidatableResponse getOrderById (){
        return given()
                .header("Content-type", "application/json")
                .queryParam("t", "")
                .when()
                .get(PATH_ORDER_API + "track")
                .then();
    }
    public ValidatableResponse AcceptOrderByCourier (int courierId, int orderId){
        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId", String.valueOf(courierId))
                .when()
                .put(PATH_ORDER_API + "accept/"+ String.valueOf(orderId))
                .then();
    }


    public ValidatableResponse AcceptOrderByCourier (int Id, boolean isOrderId){
        if (isOrderId){
            return given()
                    .header("Content-type", "application/json")
                    .queryParam("courierId", "")
                    .when()
                    .put(PATH_ORDER_API + "accept/"+ String.valueOf(Id))
                    .then();
        } else {
            return given()
                    .header("Content-type", "application/json")
                    .queryParam("courierId", String.valueOf(Id))
                    .when()
                    .put(PATH_ORDER_API + "accept/")
                    .then();
        }

    }
}
