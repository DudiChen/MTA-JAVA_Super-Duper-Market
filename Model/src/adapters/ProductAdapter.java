package adapters;

import entities.Product;
import entities.Product.PurchaseMethod;
import jaxb.generated.SDMItem;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ProductAdapter extends XmlAdapter<SDMItem, Product> {

    Product.PurchaseMethod purchaseMethod;

    @Override
    public Product unmarshal(SDMItem item) throws ValidationException {
        return new Product(item.getId(), item.getName(), purchaseMethod.valueOf(item.getPurchaseCategory()));
    }

    @Override
    public SDMItem marshal(Product v) throws Exception {
        return null;
    }
}
