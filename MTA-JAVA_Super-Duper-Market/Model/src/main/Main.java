package main;

import builder.MarketBuilder;
import entity.Market;
import entity.Product;
import exception.XMLException;
import jaxb.JaxbHandler;
import jaxb.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.ValidationException;

public class Main {
    public static void main(String[] args) {
        String XML_PATH = "/resource/ex1-big.xml";
        JaxbHandler jaxbh = new JaxbHandler();
        SuperDuperMarketDescriptor sdpMarketDescriptor = null;
        try {
            sdpMarketDescriptor = jaxbh.extractXMLData(XML_PATH);
            Market market = new MarketBuilder().build(sdpMarketDescriptor);
            System.out.println(market);
        }
        catch (XMLException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
