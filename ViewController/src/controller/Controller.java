package controller;

import builder.MarketBuilder;
import command.Executor;
import entity.Product;
import entity.market.Market;
import entity.Order;
import entity.Store;
import entity.market.OrderInvoice;
import exception.MarketIsEmptyException;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.util.Pair;
import jaxb.JaxbHandler;
import jaxb.generated.SuperDuperMarketDescriptor;
import view.View;

import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.ValidationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    Market market;
    View view;
    Executor executor;
    AtomicReference<Store> chosenStore;

    public Controller(View view) {
        this.market = new Market();
        this.view = view;
        this.view.setController(this);
        this.executor = new Executor(this);
        this.chosenStore = new AtomicReference<>();
        registerToViewEvents();
    }

    public void fetchAllStoresToUI() {
        List<Store> stores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayStores(stores);
            return;
        }
        stores = market.getAllStores();
        view.displayStores(stores);
    }

    public void loadXMLDataToUI() {
        String xmlPath = view.promptUserFilePath();
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
            view.displayError("File Not Found");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "unknown exception while loading xml file"); // TODO : figure out what to do with the exception
        }
    }

    public void addNewProduct() {
    }

    public void fetchAllProductsToUI() {
        List<Product> products = new ArrayList<>();
        List<Store> allStores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayProducts(products, allStores);
            return;
        }
        products = market.getAllProducts();
        allStores = market.getAllStores();
        view.displayProducts(products, allStores);
    }


    public void getCustomerToUI() {
    }

    private void registerToViewEvents() {
        registerOnStoreChoice();
        registerOnOrderPlaced();
        registerOnOrderAccepted();
        registerOnOrderCanceled();
        registerOnDynamicOrder();
    }

    private void registerOnDynamicOrder() {
        view.onDynamicOrder = (date, destination, productQuantityPairs) -> {
            // validate the user is not in a point where a store is
            if (productQuantityPairs.size() == 0) {
                view.showMainMenu();
                // make order dynamic
//                int orderInvoiceId = market.receiveOrder(new Order(productQuantityPairs, destination, date, chosenStore.get().getId()));
//                view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
            }
        };
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
            fetchAllProductsListToUI();
        };
    }

    private void registerOnOrderPlaced() {
        view.onOrderPlaced = (date, destination, productQuantityPairs) -> {
            StringBuilder err = new StringBuilder();
            // validate store coordinate is not the same as customer coordinate
            assert false;
            if (destination.equals(chosenStore.get().getCoordinate())) {
                err.append("cannot make order from same coordinate as store").append(System.lineSeparator());
            }
            // validate chosen products are sold by the chosen store
            for (Pair<Integer, Double> productToQuantity : productQuantityPairs) {
                int productId = productToQuantity.getKey();
                if (!chosenStore.get().isProductSold(productId)) {
                    err.append(market.getProductById(productId).getName()).append(" is not sold by ").append(market.getStoreById(chosenStore.get().getId()).getName()).append(System.lineSeparator());
                }
            }
            if (err.length() > 0) {
                throw new OrderValidationException(err.toString());
            }
            if (productQuantityPairs.size() == 0) {
                view.showMainMenu();
            }
            int orderInvoiceId = market.receiveOrder(new Order(productQuantityPairs, destination, date, chosenStore.get().getId()));
            view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
        };
    }

    public void makeDynamicOrder() {
        fetchAllProductsListToUI();
    }

    public void makeOrder() {
        fetchAllStoresListToUI();
    }

    private void fetchAllStoresListToUI() {
        List<Store> stores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayStoresList(stores);
            return;
        }
        stores = market.getAllStores();
        view.displayStoresList(stores);
    }

    private void fetchAllProductsListToUI() {
        List<Product> products = new ArrayList<>();
        List<Store> allStores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayProductsList(products, allStores);
            return;
        }
        products = market.getAllProducts();
        allStores = market.getAllStores();
        view.displayProductsList(products, allStores);
    }


    public void fetchOrdersHistoryToUI() {
        try {
            view.showOrdersHistory(market.getOrdersHistory());
        } catch (MarketIsEmptyException e) {
            view.displayError("Please Load XML First");
        }
    }

    public Executor getExecutor() {
        return executor;
    }

    public void run() {
        this.view.showMainMenu();
    }

    public String getStoreNameByID(int storeId) {
        return this.market.getStoreById(storeId).getName();
    }

    public void saveOrderHistory() {
        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
            view.displayError("Please Load XML File Before");
            return;
        }
        try {
            List<OrderInvoice> history = market.getOrdersHistory();
            if(history.size() == 0) {
                view.displayError("No History To Show");
            }
            else{
                String outputPath = view.promptUserFilePath();
                try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
                    out.writeObject(history);
                    view.fileLoadedSuccessfully();
                }
                catch (FileNotFoundException e) {
                    view.displayError("File Not Found");

                } catch (IOException e) {
                    view.displayError("Error While Writing To File");
                }
            }
        } catch (MarketIsEmptyException e) {
            view.displayError("Load XML First");
        }

    }

    public void loadOrderHistory() {
        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
            view.displayError("Please Load XML File Before");
            return;
        }
        String inputPath = view.promptUserFilePath();
        try (InputStream inputStream = new FileInputStream(inputPath)) {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            List history = (ArrayList<OrderInvoice>) in.readObject();
            market.setOrdersHistory(history);
            view.fileLoadedSuccessfully();
        } catch (FileNotFoundException e) {
            view.displayError("File Not Found");
        } catch (IOException e) {
            view.displayError("Error While Writing To File");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}