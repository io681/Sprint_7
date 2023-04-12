import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import ru.praktikum_services.qa_scooter.api.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.*;

@DisplayName("Создание курьера")
public class CreateCourierTest {
    private CourierApi courierApi;
    @Before
    public void setUp() {
        courierApi = new CourierApi();
    }
    @Test
    @DisplayName("Успешный запрос создания курьера")
    public void createNewCourierSuccessTest() {
        ValidatableResponse response = courierApi.createCourier();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает ok:true",
                true, response.extract().path("ok"));
    }
    @Test
    @DisplayName("Проверка создания двух одинаковых курьеров")
    public void createDoubleCourierIncorrectTest() {
        courierApi = new CourierApi();
        courierApi.createCourier();
        ValidatableResponse response = courierApi.createCourier();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_CONFLICT, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Этот логин уже используется. Попробуйте другой.", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка запроса создания курьера без обязательного поля")
    public void checkRequiredFieldsCreateCourierTest (){
        CourierApi courierApi2 = new CourierApi(randomCourierWithoutPassword());
//        courierApi.setCourier(randomCourierWithoutPassword());
        ValidatableResponse response = courierApi2.createCourier();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для создания учетной записи", response.extract().path("message"));
    }
    @After
    public void cleanTestData(){
        //авторизация курьера и получение id курьера
        Courier courier = courierApi.getCourier();
        Integer courierId = courierApi
                .loginCourier(courier.getLogin(),courier.getPassword())
                .extract().path("id");
        //удаление курьера
        if (courierId != null) {
            courierApi.deleteCourier(courierId);
        }
    }
}
