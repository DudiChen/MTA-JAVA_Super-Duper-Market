package view;

import controller.Controller;
import entity.*;
import entity.market.OrderInvoice;
import exception.OrderValidationException;
import javafx.util.Pair;
import view.menu.MainMenu;
import view.menu.MainMenuItemInitializer;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ConsoleView extends View {
    //    private String mainMenuHeader = "";
    MainMenu mainMenu;
    int chosenStoreId;
    Controller controller;

    @Override
    public void showMainMenu() {
        mainMenu.show();
        chosenStoreId = -1;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        this.mainMenu = MainMenuItemInitializer.initialize(this.controller);
    }

    @Override
    public void displayStoresList(List<Store> stores) {
        if (null == stores || stores.size() == 0) {
            System.out.println("Currently No Stores Exist In System. Please Load Data First");
            return;
        }
        System.out.println("Please Choose A Store To Buy From: ");
        for (int i = 0; i < stores.size(); i++) {
            System.out.println((i + 1) + ". " + stores.get(i).getName() + ", ID: " + stores.get(i).getId() + ", PPK: " + stores.get(i).getPpk());
        }
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);
        boolean isParsed = true;
        do {
            try {
                scanner = new Scanner(System.in);
                userInput = scanner.nextInt();
                if(userInput > stores.size() || userInput < 1){
                    throw new InputMismatchException();
                }
                else {
                    isParsed = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Input Not Valid, Please Try Again");
                isParsed = false;
            }
        }
        while (!isParsed);
        this.chosenStoreId = stores.get(userInput - 1).getId();
        onStoreIdChoice.accept(this.chosenStoreId);
    }


    @Override
    public void displayProductsList(List<Product> allProducts, List<Store> allStores) throws IllegalFormatException {
        if (null == allProducts || allProducts.size() == 0) {
            System.out.println("Currently No Products Exist In System. Please Load Data First");
            return;
        }
        // get delivery date from user
        System.out.println("Please Enter Delivery Date (Date Format Must Be dd/mm-hh:mm)");
        Scanner scanner = new Scanner(System.in);
        boolean isParsed = true;
        DateFormat format = new SimpleDateFormat("dd/mm-hh:mm", Locale.ENGLISH);
        format.setLenient(false);
        Date date = null;
        do {
            try {
                String userInput = scanner.next();
                date = format.parse(userInput);
                userInput = userInput.split("-")[0] + "/" + Calendar.getInstance().get(Calendar.YEAR) + "-" + userInput.split("-")[1];
                format = new SimpleDateFormat("dd/mm/yyyy-hh:mm", Locale.ENGLISH);
                date = format.parse(userInput);
                isParsed = true;
            } catch (InputMismatchException | ParseException e) {
                System.out.println("Input Not Valid, Please Try Again");
                isParsed = false;
            }
        }
        while (!isParsed);

        // get coordinates from user
        System.out.println("Please Enter Coordinate For Delivery (Coordinate Format Must Be x:y, Where x And y Are In The Range 0 - 50):");
        isParsed = true;
        Point point = null;
        do {
            try {
                String input = scanner.next();
                String[] inputParts = input.split(":");
                if (inputParts.length != 2) {
                    throw new IllegalArgumentException();
                }
                int x = Integer.parseInt(inputParts[0]);
                int y = Integer.parseInt(inputParts[1]);
                if (x > 50 || x < 0 || y > 50 || y < 0) {
                    throw new NumberFormatException();
                }
                point = new Point(x, y);
                isParsed = true;
            } catch (NumberFormatException e) {
                System.out.println("Please Use Only Numbers Between 0 and 50");
                isParsed = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Please Stick To The Format x:y");
                isParsed = false;
            }
        }
        while (!isParsed);

        // get products choices
        Optional<Store> currentChosenStore = Optional.empty();
        if (chosenStoreId != -1) {
            currentChosenStore = allStores.stream().filter(store -> store.getId() == chosenStoreId).findFirst();
        }
        Optional<Store> finalCurrentChosenStore = currentChosenStore;
        for (int i = 0; i < allProducts.size(); i++) {
            System.out.print((i + 1) + ". " + allProducts.get(i).getName() + ", ID: " + allProducts.get(i).getId() + ", Purchase Method: " + allProducts.get(i).getPurchaseMethod().toString());
            if (finalCurrentChosenStore.isPresent() &&
                    finalCurrentChosenStore.get().isProductSold(allProducts.get(i).getId())) {
                System.out.println(", Price In " + finalCurrentChosenStore.get().getName() + ": " + finalCurrentChosenStore.get().getPriceOfProduct(allProducts.get(i).getId()));
            } else if (finalCurrentChosenStore.isPresent()) {
                System.out.println(", Not Sold By " + finalCurrentChosenStore.get().getName());
            } else {
                System.out.println();
            }
            this.chosenStoreId = -1;
        }
        isParsed = true;
        boolean doneChoosing = false;
        System.out.println();
        System.out.println("Please Choose a Product And Quantity (Format Must Be x:y Where x is Serial Number (Not ID) Of Product And y Is Quantity (If Product Is Sold By Weight It Can Also Contain Numbers After" + System.lineSeparator() + ". Press q to stop)):");
        List<Pair<Integer, Double>> chosenProductToQuantity = new ArrayList<>();
        while (!doneChoosing) {
            do {
                try {
                    String input = scanner.next();
                    if (input.toLowerCase().equals("q")) {
                        doneChoosing = true;
                        break;
                    }
                    String[] inputParts = input.split(":");
                    Product chosenProduct;
                    double quantity = 0;
                    if (inputParts.length != 2) {
                        throw new IllegalArgumentException();
                    }
                    int x = Integer.parseInt(inputParts[0]);
                    if(x < 1 || x > allProducts.size()){
                        throw new ArrayIndexOutOfBoundsException();
                    }
                    chosenProduct = allProducts.get(x - 1);
                    String y = inputParts[1];
                    if (chosenProduct.getPurchaseMethod().equals(Product.PurchaseMethod.WEIGHT)) {
                        quantity = Double.parseDouble(y);
                    } else if (chosenProduct.getPurchaseMethod().equals(Product.PurchaseMethod.QUANTITY)) {
                        quantity = Integer.parseInt(y);
                    }
                    chosenProductToQuantity.add(new Pair<>(chosenProduct.getId(), quantity));
                    isParsed = true;
                } catch (NumberFormatException e) {
                    System.out.println("Input Not Valid, Please Try Again");
                    isParsed = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Please Stick To The Format x:y");
                    isParsed = false;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("x Must Be Between 1 And " + (allProducts.size() + 1));
                    isParsed = false;
                }
            }
            while (!isParsed);
        }
        try {
            if(finalCurrentChosenStore.isPresent()){
                onOrderPlaced.apply(date, point, chosenProductToQuantity);
            }
            else {
                onDynamicOrder.apply(date, point, chosenProductToQuantity);
            }
        } catch (OrderValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayStores(List<Store> allStores) {
        if (null == allStores || allStores.size() == 0) {
            System.out.println("Currently No Stores Exist In System. Please Load Data First");
            return;
        }
        allStores.forEach(store -> {
            System.out.println("====== " + store.getName() + " ====== ");
            System.out.println("Store ID: " + store.getId());
            System.out.println("Sold Products: ");
            for (Map.Entry<Integer, StoreProduct> idToStoreProduct : store.getStock().getSoldProducts().entrySet()) {
                System.out.println("        ==");
                System.out.println("    Product ID: " + idToStoreProduct.getKey());
                Product product = idToStoreProduct.getValue().getProduct();
                System.out.println("    Product Name: " + product.getName());
                System.out.println("    Purchase Method: " + product.getPurchaseMethod().toString());
                System.out.println("    Price In Store: " + idToStoreProduct.getValue().getPrice());
                System.out.println("    Total Sold By Store: " + store.getTotalProductSales(idToStoreProduct.getValue().getId()));
                if (store.getOrdersHistory().size() > 0) {
                    System.out.println("Orders Made:");
                    for (OrderInvoice orderInvoice : store.getOrdersHistory()) {
                        System.out.println("        Delivery Date: " + orderInvoice.getDeliveryDate());
                        System.out.println("        Total Amount Of Products: " + orderInvoice.getInvoiceProducts().size());
                        System.out.println("        Total Products Price: " + orderInvoice.getTotalPrice());
                        System.out.println("        Shipment Price " + orderInvoice.getShipmentPrice());
                        System.out.println("        Total Price: " + (orderInvoice.getTotalPrice() + orderInvoice.getShipmentPrice()));
                    }
                }
            }
            System.out.println("Price Per Kilometer: " + store.getPpk());
            System.out.println("Shipment Incomes Total: " + store.getTotalShipmentIncome());
            System.out.println();
        });
    }

    @Override
    public void displayProducts(List<Product> allProducts, List<Store> allStores) {
        if (null == allProducts || allProducts.size() == 0) {
            System.out.println("Currently No Products Exist In System. Please Load Data First");
            return;
        }
        allProducts.stream().forEach(product -> {
            System.out.println("====== " + product.getName() + " ====== ");
            System.out.println("ID: " + product.getId());
            System.out.println("Purchase Method: " + product.getPurchaseMethod().toString());
            int numberOfSellingStores = (int) allStores.stream()
                    .filter(store -> store.isProductSold(product.getId())).count();
            System.out.println("Total Selling Stores: " + numberOfSellingStores);
            double averagePrice = allStores.stream()
                    .map(Store::getStock)
                    .map(stock -> stock.getSoldProducts().values())
                    .flatMap(Collection::stream)
                    .filter(storeProduct -> storeProduct.getId() == product.getId())
                    .map(StoreProduct::getPrice)
                    .reduce((double) 0, (acc, curr) -> acc += curr)
                    / numberOfSellingStores;
            System.out.println("Average Price: " + averagePrice);
            int numberOfTotalProductSales = allStores.stream()
                    .map(store -> store.getTotalProductSales(product.getId()))
                    .reduce(0, (acc, curr) -> acc += curr);
            System.out.println("Total Items Sold: " + numberOfTotalProductSales);
            System.out.println();
        });
    }

    @Override
    public void summarizeOrder(OrderInvoice orderInvoice) {
        System.out.println("====== Order Summery ======");
        orderInvoice.getInvoiceProducts().stream().forEach(invoiceProduct -> {
            System.out.println("    ==== " + invoiceProduct.getName() + " ====");
            System.out.println("        ID: " + invoiceProduct.getId());
            System.out.println("        Purchase Method: " + invoiceProduct.getPurchaseMethod());
            System.out.println("        Price: " + invoiceProduct.getPrice());
            System.out.println("        Quantity: " + invoiceProduct.getQuantity());
            System.out.println("        Total Price: " + invoiceProduct.getTotalPrice());
        });
        System.out.println("* Shipment Cost: " + orderInvoice.getShipmentPrice());
        System.out.println("Total: " + orderInvoice.getTotalPrice());
        System.out.println("Approve Order? (Y/N)");
        Scanner scanner = new Scanner(System.in);
        boolean isParsed = true;
        do {
            try {
                String userInput = scanner.next();
                if (userInput.toLowerCase().equals("n")) {
                    onOrderCanceled.accept(orderInvoice.getOrderId());
                    System.out.println("Order Canceled");
                    isParsed = true;
                } else if (userInput.toLowerCase().equals("y")) {
                    onOrderAccepted.accept(orderInvoice.getOrderId());
                    System.out.println("Order Approved");
                    isParsed = true;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("Input Not Valid, Please Try Again");
                isParsed = false;
            }
        }
        while (!isParsed);
    }

    @Override
    public void displayError(String message) {
        System.out.println(message);
    }

    @Override
    public void fileLoadedSuccessfully() {
        System.out.println("Successfully loaded file");
    }

    @Override
    public void showOrdersHistory(List<OrderInvoice> ordersHistory) {
        if (ordersHistory.isEmpty()) {
            System.out.println("No Orders To Show Or No Data Loaded");
        } else {
            ordersHistory.stream().forEach(orderInvoice -> {
                System.out.println("======");
                System.out.println("Order ID: " + orderInvoice.getOrderId());
                System.out.println("Delivery Date: " + orderInvoice.getDeliveryDate());
                System.out.println("Providing Store: " + this.controller.getStoreNameByID(orderInvoice.getStoreId()) + ", ID: " + orderInvoice.getStoreId());
                System.out.println("Number Of Items: " + orderInvoice.getInvoiceProducts().size()); /// not sure about this one - 5.4
                System.out.println("Total Products Price: " + (orderInvoice.getTotalPrice() - orderInvoice.getShipmentPrice()));
                System.out.println("Shipment Price: " + orderInvoice.getShipmentPrice());
                System.out.println("Total Price: " + orderInvoice.getTotalPrice());
                System.out.println("======");
            });
        }
    }

    // TODO: [VIEW][DUDI][MENUS]: bug when waiting for any key
    // TODO: [View][Dudi]: Add view prompt for xml path
    @Override
    public String promptUserFilePath() {
        System.out.println("Please Enter Path:");
        return new Scanner(System.in).next();
        //      "/Users/noamwa/Desktop/Studies/java/ex02-sdp/MTA-JAVA_Super-Duper-Market/Model/src/resource/ex1-big.xml";
        //      "/Users/noamwa/Desktop/Studies/java/ex02-sdp/MTA-JAVA_Super-Duper-Market/Model/src/resource/test";
    }
}


//50/50-45:90
