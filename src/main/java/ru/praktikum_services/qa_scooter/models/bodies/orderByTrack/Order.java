package ru.praktikum_services.qa_scooter.models.bodies.orderByTrack;

import ru.praktikum_services.qa_scooter.models.bodies.listOrders.Orders;

public class Order extends Orders {
    private String id;
    private boolean cancelled;
    private boolean finished;
    private boolean inDelivery;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isInDelivery() {
        return inDelivery;
    }

    public void setInDelivery(boolean inDelivery) {
        this.inDelivery = inDelivery;
    }
}
