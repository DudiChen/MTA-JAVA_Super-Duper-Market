package main;

import builder.MarketBuilder;
import entity.Market;
import entity.Product;
import exception.XMLException;
import jaxb.JaxbHandler;
import jaxb.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.ValidationException;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String XML_PATH = "/resource/ex1-error-3.6.xml";
        JaxbHandler jaxbh = new JaxbHandler();
        SuperDuperMarketDescriptor sdpMarketDescriptor = null;
        try {
            sdpMarketDescriptor = jaxbh.extractXMLData(XML_PATH);
            Market market = new MarketBuilder().build(sdpMarketDescriptor);
            System.out.println(market);
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (XMLException e) {
            e.printStackTrace();
        }
    }
}
