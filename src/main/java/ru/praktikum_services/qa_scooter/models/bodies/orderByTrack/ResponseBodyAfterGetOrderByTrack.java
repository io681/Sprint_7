package ru.praktikum_services.qa_scooter.models.bodies.orderByTrack;

import ru.praktikum_services.qa_scooter.models.bodies.listOrders.Orders;

public class ResponseBodyAfterGetOrderByTrack extends Orders {
private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrderBy(Order orderBy) {
        this.order = orderBy;
    }

//public int getIdByOrder (){
//    return orderBy.getId();
//}
}
