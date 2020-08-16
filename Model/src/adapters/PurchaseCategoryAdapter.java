package adapters;

//import entities.Product.PurchaseMethod;
import entities.Product;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PurchaseCategoryAdapter extends XmlAdapter<String, Product.PurchaseMethod> {
    @Override
    public Product.PurchaseMethod unmarshal(String v) throws ValidationException {
        return Product.PurchaseMethod.valueOf(v);
    }

    @Override
    public String marshal(Product.PurchaseMethod pm) throws Exception {
        return pm.toString();
    }
}
