package entities;

public class OLD_Product {
    private OLD_ID OLD_Id;
    private String name;
    private OLD_PurchaseCategory OLD_PurchaseCategory;

    public String getName() {
        return name;
    }

    public OLD_Product(OLD_ID OLD_Id, String name, OLD_PurchaseCategory OLD_PurchaseCategory) {
        this.OLD_Id = OLD_Id;
        this.name = name;
        this.OLD_PurchaseCategory = OLD_PurchaseCategory;
    }


    public OLD_ID getOLD_Id() {
        return OLD_Id;
    }
}
