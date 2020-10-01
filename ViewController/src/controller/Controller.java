package controller;

import command.Executor;
import entity.*;
import entity.market.Market;
import entity.market.OrderInvoice;
import exception.MarketIsEmptyException;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import view.View;
import view.menu.item.CustomerMapElement;
import view.menu.item.StoreMapElement;

import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private View view;
    private Executor executor;
    private AtomicReference<Store> chosenStore;
    private boolean loaded = false;
    private Market market;
//    private AtomicReference<Integer> currentCustomerId;
    private int currentCustomerId;

    public Controller(View view) {
        this.view = view;
        view.setController(this);
        this.market = new Market();
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
        stores = this.market.getAllStores();
        view.displayStores(stores);
    }

    public void loadXMLDataToUI() {
        String xmlPath = view.promptUserFilePath();
        loadXMLData(xmlPath);
    }

    public void loadXMLData(String fullFilePath) {

        LoadXmlTask loadXmlTask = new LoadXmlTask(fullFilePath);
        loadXmlTask.setOnSucceeded(event -> {
            try {
                this.market = loadXmlTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            view.fileLoadedSuccessfully();
            view.showMainMenu();
        });
        loadXmlTask.setOnFailed(event -> {
            Throwable exception = loadXmlTask.getException();
            if (exception instanceof ValidationException) {
                view.displayError(exception.getMessage());
            }
            if (exception instanceof XMLParseException || exception instanceof XMLException || exception instanceof FileNotFoundException) {
                view.displayError("File Not Found");
            } else {
                System.out.println(exception.getMessage() + "unknown exception while loading xml file"); // TODO : figure out what to do with the exception
            }
        });
        this.bindTaskToUIComponents(loadXmlTask);
        new Thread(loadXmlTask).start();
    }

    private void bindTaskToUIComponents(LoadXmlTask loadXmlTask) {
        // task message
        view.xmlProgressStateProperty().bind(loadXmlTask.messageProperty());
        view.xmlProgressBarProperty().bind(loadXmlTask.progressProperty());

        loadXmlTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            view.xmlProgressStateProperty().unbind();
            view.xmlProgressBarProperty().unbind();
        });
    }

    public void addNewProduct(int storeId, int productId) {
        this.market.addProductToStore(storeId, productId, 0);
        this.view.onStoreIdChoice.accept(storeId);
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
        view.onDynamicOrder = (date, customerId, productQuantityPairs) -> {
            this.market.getAllStores()
                    .forEach(store -> {
                        Optional<List<Pair<Integer, Double>>> maybeOrder = findCheapestOrderForStore(store, productQuantityPairs.getKey());
                        if (maybeOrder.isPresent()) {
                            this.chosenStore.set(store);
                            List<Pair<Integer, Double>> orderPairs = maybeOrder.get();
                            try {
                                List<Pair<Integer, Double>> toDelete = new ArrayList<>();
                                for (Pair<Integer, Double> pair : productQuantityPairs.getKey()) {
                                    for (Pair<Integer, Double> pair1 : orderPairs) {
                                        if (pair.getKey() == pair1.getKey()) {
                                            toDelete.add(pair);
                                            break;
                                        }
                                    }
                                }
                                productQuantityPairs.getKey().removeAll(toDelete);
                                makeOrderForChosenStore(date, customerId, new Pair(orderPairs, productQuantityPairs.getValue()));
                            } catch (OrderValidationException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        };
    }

    private Optional<List<Pair<Integer, Double>>> findCheapestOrderForStore(Store store, List<Pair<Integer, Double>> productQuantityPairs) {

        List<Pair<Integer, Double>> res = new ArrayList<>();
        productQuantityPairs
                .forEach(pair -> {
                    this.market.getAllStores().stream()
                            .mapToDouble(storeToCheckPriceIn -> {
                                double price;
                                try {
                                    price = store.getPriceOfProduct(pair.getKey());
                                } catch (NoSuchElementException e) {
                                    price = Double.MAX_VALUE;
                                }
                                return price;
                            })
                            .min()
                            .ifPresent(minPrice -> {
                                try {
                                    double priceInCurrentStore = store.getPriceOfProduct(pair.getKey());
                                    if (minPrice == priceInCurrentStore) {
                                        res.add(new Pair<>(pair.getKey(), pair.getValue()));
                                    }
                                } catch (NoSuchElementException e) {

                                }
                            });
                });
        if (res.isEmpty()) {
            return Optional.empty();
        }
        System.out.println(res);
        return Optional.of(res);
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
        view.onOrderPlaced = this::makeOrderForChosenStore;
    }

    private void makeOrderForChosenStore(Date date, Integer customerId, Pair<List<Pair<Integer, Double>>, List<Discount>> productQuantityPairsWithDiscounts) throws OrderValidationException {
        StringBuilder err = new StringBuilder();
        // TODO :: use chosen discounts and customer id!
        List<Discount> chosenDiscounts = productQuantityPairsWithDiscounts.getValue();
        // validate store coordinate is not the same as customer coordinate
        assert false;
        if (this.market.getCustomerById(customerId).getLocation().equals(chosenStore.get().getCoordinate())) {
            err.append("cannot make order from same coordinate as store").append(System.lineSeparator());
        }
        // validate chosen products are sold by the chosen store
        for (Pair<Integer, Double> productToQuantity : productQuantityPairsWithDiscounts.getKey()) {
            int productId = productToQuantity.getKey();
            if (!chosenStore.get().isProductSold(productId)) {
                err.append(market.getProductById(productId).getName()).append(" is not sold by ").append(market.getStoreById(chosenStore.get().getId()).getName()).append(System.lineSeparator());
            }
        }
        if (err.length() > 0) {
            throw new OrderValidationException(err.toString());
        }
        if (productQuantityPairsWithDiscounts.getKey().size() == 0) {
            view.showMainMenu();
        }
        int orderInvoiceId = market.receiveOrder(new Order(productQuantityPairsWithDiscounts.getKey(), this.market.getCustomerById(customerId).getLocation(), date, chosenStore.get().getId()));
        view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
    }

    public void makeDynamicOrder() {
        fetchAllProductsListToUI();
    }

    public void makeOrder() {
        fetchAllStoresListToUI();
    }

    public void fetchAllStoresListToUI() {
        List<Store> stores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayStoresList(stores);
            return;
        }
        stores = market.getAllStores();
        view.displayStoresList(stores);
    }

    public void fetchAllProductsListToUI() {
        List<Product> products = new ArrayList<>();
        List<Store> allStores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayProductsForStore(products, chosenStore.get());
            return;
        }
        products = market.getAllProducts()
                .stream().filter(product -> chosenStore.get().isProductSold(product.getId())).collect(Collectors.toList());
        allStores = market.getAllStores();
        view.displayProductsForStore(products, chosenStore.get());
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
            if (history.size() == 0) {
                view.displayError("No History To Show");
            } else {
                String outputPath = view.promptUserFilePath();
                try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
                    out.writeObject(history);
                    view.fileLoadedSuccessfully();
                } catch (FileNotFoundException e) {
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

    public Store getStoreById(int storeId) {
        return this.market.getStoreById(storeId);
    }

    public void fetchMapToUI() {
        this.view.showMap(
                Stream.concat(
                        this.market.getAllStores().stream()
                                .map(store -> new StoreMapElement(store, view.onStoreIdChoice)),
                        this.market.getAllCustomers().stream()
                                .map(customer -> new CustomerMapElement(customer))
                )
                        .collect(Collectors.toList()));
    }

    public Product getProductById(int productId) {
        return this.market.getProductById(productId);
    }

    public boolean isAvailableDiscount(List orderProducts, List chosenDiscounts) {
        return true;
    }

    public List<Customer> getAllCustomers() {
        return this.market.getAllCustomers();
    }

    public void deleteProduct(int productId, int storeId) {
        this.market.deleteProductForStore(productId, storeId);
    }

    public void changePriceForProduct(int storeId, int productId, double newPrice) {
        this.market.changePriceForProduct(storeId, productId, newPrice);
    }

    public List<Product> getAllProducts() {
        return this.market.getAllProducts();
    }
}