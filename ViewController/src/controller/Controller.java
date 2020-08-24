package controller;

import builder.MarketBuilder;
import entity.market.Market;
import entity.Order;
import entity.Store;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.util.Pair;
import jaxb.JaxbHandler;
import jaxb.generated.SuperDuperMarketDescriptor;
import view.View;
import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.ValidationException;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    Market market;
    View view;

    public Controller() {
        this.market = new Market();
    }

    public void fetchAllStoresToUI() {
        view.displayStores(market.getAllStores());
    }

    public void loadXMLData(String fullFilePath) { //"/resource/ex1-big.xml"
        JaxbHandler jaxbHandler = new JaxbHandler();
        SuperDuperMarketDescriptor sdpMarketDescriptor = null;
        try {
            sdpMarketDescriptor = jaxbHandler.extractXMLData(fullFilePath);
            this.market = new MarketBuilder().build(sdpMarketDescriptor);
            view.fileLoadedSuccessfully();
        } catch (ValidationException e) {
            view.displayError(e.getMessage());
        } catch (XMLParseException e) {
            e.printStackTrace();
        } catch (XMLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e); // TODO : figure out what to do with the exception
        }
    }

    public void addNewProduct() {
    }

    public void fetchAllProductsToUI() {
        view.displayProducts(market.getAllProducts()); // TODO: mark products that are sold by chosenStore
    }

    public void getCustomerToUI() {
    }

    public void makeOrder() {
        fetchAllStoresToUI();
        AtomicReference<Store> chosenStore = null;
        view.onStoreIdChoice = (storeId) -> {
            assert false;
            chosenStore.set(market.getStoreById(storeId));
            fetchAllProductsToUI();
        };
        view.onOrderPlaced = (date, destination, productPricePair) -> {
            StringBuilder err = new StringBuilder();
            // validate store coordination is not the same as customer coordinate
            assert false;
            if (destination.equals(chosenStore.get().getCoordinate())) {
                err.append("cannot make order from same coordinate as store").append(System.lineSeparator());
            }
            // validate chosen products are sold by the chosen store
            for (Pair<Integer, Integer> productToQuantity : productPricePair) {
                int productId = productToQuantity.getKey();
                if (!chosenStore.get().isProductSold(productId)) {
                    err.append(market.getProductById(productId).toString()).append(" is not sold by ").append(market.getStoreById(chosenStore.get().getId())).append(System.lineSeparator());
                }
            }
            if (err.length() > 0) {
                throw new OrderValidationException(err.toString());
            }
            int orderInvoiceId = market.receiveOrder(new Order(productPricePair, destination, date), chosenStore.get().getId());
            view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
        };
        view.onOrderAccepted = (orderInvoiceId) -> market.approveOrder(orderInvoiceId);
        view.onOrderCanceled = (orderInvoiceId) -> market.cancelOrder(orderInvoiceId);
    }

    public void fetchOrdersHistoryToUI() {
        view.showOrdersHistory(market.getOrdersHistory());
    }
}
