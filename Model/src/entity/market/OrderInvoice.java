package entity.market;

import entity.Order;
import entity.StoreProduct;

import java.util.Date;
import java.util.List;

public class OrderInvoice {

    private OrderStatus orderStatus = OrderStatus.ISSUED;
    private int orderId;
    private List<InvoiceProduct> invoiceProduct;
    private double totalPrice;
    private Date deliveryDate;

    public OrderInvoice(int id, List<InvoiceProduct> invoiceProducts, double totalPrice, Date deliveryDate) {
        this.orderId = id;
        this.invoiceProduct = invoiceProducts;
        this.totalPrice = totalPrice;
        this.deliveryDate = deliveryDate;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<InvoiceProduct> getInvoiceProduct() {
        return invoiceProduct;
    }

    public double getTotalPrice() {
        return totalPrice;
    }


    enum OrderStatus {
        ACCEPTED, CANCELED, ISSUED
    }
}
