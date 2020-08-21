package entity;

public class Product {
    public enum PurchaseMethod {
        QUANTITY("Quantity"),
        WEIGHT("Weight");

        private String name;

        PurchaseMethod(String name) {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private int id;
    private String name;
    private PurchaseMethod purchaseMethod;

    public Product(int id, String name, PurchaseMethod purchaseMethod) {
        this.id = id;
        this.name = name;
        this.purchaseMethod = purchaseMethod;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PurchaseMethod getPurchaseMethod() {
        return purchaseMethod;
    }
}
