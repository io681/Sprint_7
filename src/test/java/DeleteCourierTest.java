import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.CourierApi;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;

@DisplayName("Удаление курьера")
public class DeleteCourierTest {
    private Courier courier;
    private int courierId;
    private CourierApi courierApi;
    @Before
    public void setUp() {
        //создание курьера
        courierApi = new CourierApi();
        courier = randomCourier();
        courierApi.createCourier(courier);

        //авторизация курьера для получения id курьера
        courierId = courierApi
                .loginCourier(courier.getLogin(),courier.getPassword())
                .extract().path("id");
    }
    @Test
    @DisplayName("Успешное создание курьера")
    public void DeleteSuccessCourierTest() {
        ValidatableResponse response = courierApi.deleteCourier(courierId);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает ok:true",
                true, response.extract().path("ok"));
    }
    @Test
    @DisplayName("Некорректное удаление без id курьера")
    @Description("Тест будет падать, т.к. сервер возвращает другую ошибку 404 и  другой message (не по спецификации)")
    public void DeleteCourierWithoutCourierIdTest() {
        ValidatableResponse response = courierApi.deleteCourier();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для удаления курьера", response.extract().path("message"));
    }
    @Test
    @DisplayName("Некорректное удаление с несуществующим id курьера")
    public void DeleteCourierWithFakeCourierIdTest() {
        int fakeCourierId = 999999;
        ValidatableResponse response = courierApi.deleteCourier(fakeCourierId);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_NOT_FOUND, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Курьера с таким id нет.", response.extract().path("message"));
    }
    @After
    public void cleanTestData(){
        //авторизация курьера и получение id курьера
        Integer courierId = courierApi
                .loginCourier(courier.getLogin(),courier.getPassword())
                .extract().path("id");

        //удаление курьера
        if (courierId != null) {
            courierApi.deleteCourier(courierId);
        }

    }
}
