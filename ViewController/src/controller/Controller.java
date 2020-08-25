package controller;

import builder.MarketBuilder;
import command.Executor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    Market market;
    View view;
    Executor executor;
    AtomicReference<Store> chosenStore = null;


    public Controller(View view) {
        this.market = new Market();
        this.view = view;
        this.view.setController(this);
        this.executor = new Executor(this);
        registerToViewEvents();
    }

    public void fetchAllStoresToUI() {
        List<Store> stores = new ArrayList<>();
        if(market == null || market.isEmpty()) {
            view.displayStores(stores);
            return;
        }
        stores = market.getAllStores();
        view.displayStores(stores);
    }

    public void loadXMLDataToUI() {
        String xmlPath = view.promptUserXmlFilePath();
        loadXMLData(xmlPath);
    }

    public void loadXMLData(String fullFilePath) {
        JaxbHandler jaxbHandler = new JaxbHandler();
        SuperDuperMarketDescriptor sdpMarketDescriptor = null;
        try {
            sdpMarketDescriptor = jaxbHandler.extractXMLData(fullFilePath);
            this.market = new MarketBuilder().build(sdpMarketDescriptor);
            view.fileLoadedSuccessfully();
        } catch (ValidationException e) {
            view.displayError(e.getMessage());
        } catch (XMLParseException | XMLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage() + "unknown exception while loading xml file"); // TODO : figure out what to do with the exception
        }
    }

    public void addNewProduct() {
    }

    public void fetchAllProductsToUI() {
        view.displayProducts(market.getAllProducts()); // TODO: mark products that are sold by chosenStore
    }

    public void getCustomerToUI() {
    }

    private void registerToViewEvents() {
        registerOnStoreChoice();
        registerOnOrderPlaced();
        registerOnOrderAccepted();
        registerOnOrderCanceled();
    }

    private void registerOnOrderAccepted() {
        view.onOrderAccepted = (orderInvoiceId) -> market.approveOrder(orderInvoiceId);
    }

    private void registerOnOrderCanceled() {
        view.onOrderCanceled = (orderInvoiceId) -> market.cancelOrder(orderInvoiceId);
    }

    private void registerOnStoreChoice() {
        view.onStoreIdChoice = (storeId) -> {
            assert false;
            chosenStore.set(market.getStoreById(storeId));
            fetchAllProductsToUI();
        };
    }

    private void registerOnOrderPlaced() {
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
            int orderInvoiceId = market.receiveOrder(new Order(productPricePair, destination, date, chosenStore.get().getId()));
            view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
        };
    }

    public void makeOrder() {
        fetchAllStoresToUI();
    }

    public void fetchOrdersHistoryToUI() {
        view.showOrdersHistory(market.getOrdersHistory());
    }

    public Executor getExecutor() {
        return executor;
    }

    public void run() {
        this.view.showMainMenu();
    }
}
