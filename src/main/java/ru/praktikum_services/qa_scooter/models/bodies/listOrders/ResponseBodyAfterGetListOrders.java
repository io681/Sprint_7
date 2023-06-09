package ru.praktikum_services.qa_scooter.models.bodies.listOrders;

import java.util.List;

public class ResponseBodyAfterGetListOrders {
    private List<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
}
