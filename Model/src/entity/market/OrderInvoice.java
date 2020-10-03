package entity.market;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class OrderInvoice implements Serializable {

    private OrderStatus orderStatus = OrderStatus.ISSUED;
    private int orderId;
    private int customerId;
    private List<InvoiceProduct> invoiceProduct;
    private List<InvoiceDiscountProduct> discountProducts;
    private double totalPrice;
    private Date deliveryDate;
    private int storeId;
    private double shipmentPrice;

    public double getShipmentPrice() {
        return shipmentPrice;
    }

    public OrderInvoice(int id, List<InvoiceProduct> invoiceProducts, List<InvoiceDiscountProduct> discountProducts, double totalPrice, Date deliveryDate, int storeId, double shipmentPrice) {
        this.orderId = id;
        this.invoiceProduct = invoiceProducts;
        this.discountProducts = discountProducts;
        this.totalPrice = totalPrice;
        this.deliveryDate = deliveryDate;
        this.storeId = storeId;
        this.shipmentPrice = shipmentPrice;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProduct;
    }

    public List<InvoiceDiscountProduct> getDiscountProducts() {
        return discountProducts;
    }

    public void setDiscountProducts(List<InvoiceDiscountProduct> value) {
        this.discountProducts = value;
        for (InvoiceDiscountProduct discountProduct : this.discountProducts) {
            this.totalPrice += discountProduct.getAdditionalCost();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getStoreId() {
        return storeId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }


    enum OrderStatus implements Serializable {
        ACCEPTED, CANCELED, ISSUED
    }
}
