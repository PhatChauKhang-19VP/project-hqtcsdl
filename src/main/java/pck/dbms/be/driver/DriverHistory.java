package pck.dbms.be.driver;

import pck.dbms.be.order.Order;

public class DriverHistory {
    private Order order;
    private Driver driver;
    private double income;

    public DriverHistory() {
    }

    public DriverHistory(Order order, Driver driver, double income) {
        this.order = order;
        this.driver = driver;
        this.income = income;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
