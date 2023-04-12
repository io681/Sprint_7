import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.CourierApi;
import ru.praktikum_services.qa_scooter.api.OrderApi;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


@DisplayName("Принять заказ")
public class AcceptOrderTest {
    private CourierApi courierApi;
    private int courierId;
    private int orderId;
    private OrderApi orderApi;

    @Before
    public void setUp() {
        //создание курьера и авторизация для получения id курьера
        courierApi = new CourierApi();
        courierApi.createCourier();
        Courier courier = courierApi.getCourier();
        courierId = courierApi
                .loginCourier(courier.getLogin(),courier.getPassword())
                .extract().path("id");

        //создать заказ и получить номер track
        orderApi = new OrderApi();
        int trackNumber = orderApi
                .createOrder(Arrays.asList("GREY"))
                .extract().path("track");
        //запросить заказ по номеру track и  получить id заказа
        orderId = orderApi
                .getOrderById(trackNumber)
                .extract().path("order.id");
    }
    @Test
    @DisplayName("Проверка успешного принятия заказа")
    public void successAcceptOrderTest (){

        ValidatableResponse response = orderApi.AcceptOrderByCourier(courierId,orderId);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает ok:true",
                true, response.extract().path("ok"));
    }
    @Test
    @DisplayName("Проверка запроса без id курьера")
    public void acceptOrderWithoutCourierIdTest (){
        ValidatableResponse response = orderApi.AcceptOrderByCourier(orderId, true);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для поиска", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка запроса с неверным id курьера")
    public void acceptOrderWithIncorrectCourierIdTest (){
        int IncorrectCourierId = 999999;
        ValidatableResponse response = orderApi.AcceptOrderByCourier(IncorrectCourierId,orderId);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_NOT_FOUND, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Курьера с таким id не существует", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка запроса без id заказа")
    @Description("Тест будет падать, т.к. сервер возвращает другую ошибку 404 и  другой message (не по спецификации)")
    public void acceptOrderWithoutOrderIdTest (){
        ValidatableResponse response = orderApi.AcceptOrderByCourier(courierId, false);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для поиска", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка запроса c неверным id заказа")
    public void acceptOrderWithIncorrectOrderIdTest (){
        int IncorrectOrderId = 999999;
        ValidatableResponse response = orderApi.AcceptOrderByCourier(courierId,IncorrectOrderId);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_NOT_FOUND, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Заказа с таким id не существует", response.extract().path("message"));
    }
    //удаление курьера
    @After
    public void cleanTestData(){
        courierApi.deleteCourier(courierId);
    }

}
