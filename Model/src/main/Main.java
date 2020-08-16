package main;

import entities.Product;
import exceptions.XMLException;
import jaxb.JaxbHandler;
import jaxb.generated.SuperDuperMarketDescriptor;

public class Main {
    public static void main(String[] args) {
        Product.PurchaseMethod pm = Product.PurchaseMethod.QUANTITY;
        System.out.println(pm);

        String XML_PATH = "/resources/ex1-big.xml";
        JaxbHandler jaxbh = new JaxbHandler();
        SuperDuperMarketDescriptor sdpMarketDescriptor = null;
        try {
            sdpMarketDescriptor = jaxbh.extractXMLData(XML_PATH);
        }
        catch (XMLException e) {
            System.out.println(e.getMessage());
        }
    }
}
