import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.OrderApi;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Получить заказ по его номеру")
public class OrderByTrackTest {
    private OrderApi orderApi;
    private int trackNumber;
    @Before
    public void setUp() {
        //создание заказа и  получение номера track
        orderApi = new OrderApi();
        trackNumber = orderApi
                .createOrder(Arrays.asList("GREY"))
                .extract().path("track");
    }
    @Test
    @DisplayName("Проверка получения заказа по его track-номеру")
    public void getOrderByIdSuccessTest (){
        ValidatableResponse response = orderApi.getOrderById(trackNumber);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertNotNull("Пустой orders в респонзе", response.extract().path("order.id"));
    }
    @Test
    @DisplayName("Проверка получения заказа без track-номера")
    public void getOrderByIdWithoutTrackIdTest (){
        ValidatableResponse response = orderApi.getOrderById();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для поиска", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка получения заказа с несуществующим track-номером")
    public void getOrderByIncorrectIdTest (){
        int incorrectTrackNumber = 999999;
        ValidatableResponse response = orderApi.getOrderById(incorrectTrackNumber);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_NOT_FOUND, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Заказ не найден", response.extract().path("message"));
    }
}
