package view;

import controller.Controller;
import entity.Order;
import entity.Product;
import entity.Store;
import entity.StoreProduct;
import entity.market.OrderInvoice;
import view.menu.MainMenu;
import view.menu.MainMenuItemInitializer;

import java.util.List;
import java.util.Map;

public class ConsoleView extends View {
    //    private String mainMenuHeader = "";
    MainMenu mainMenu;

    @Override
    public void showMainMenu() {
//        System.out.println(mainMenuHeader);
        mainMenu.show();
    }

    @Override
    public void setController(Controller controller) {
        this.mainMenu = MainMenuItemInitializer.initialize(controller);
    }

//    private int getUserChoice() {
//        Scanner scanner = new Scanner(System.in);
//        boolean isValidInput
//    }

    @Override
    public void displayStores(List<Store> allStores) {
        allStores.forEach(store -> {
            System.out.println("Store ID: " + store.getId());
            System.out.println("Name: " + store.getName());
            System.out.println("Sold Products: ");
            for (Map.Entry<Integer, StoreProduct> idToStoreProduct : store.getStock().getSoldProducts().entrySet()) {
                System.out.println("    Product ID: " + idToStoreProduct.getKey());
                Product product = idToStoreProduct.getValue().getProduct();
                System.out.println("    Product Name: " + product.getName());
                System.out.println("    Purchase Method: " + product.getPurchaseMethod().toString());
                System.out.println("    Price In Store: " + idToStoreProduct.getValue().getPrice());
                System.out.println("    Total Products Sold: " + store.getNumberOfTimesAProductWasSold());
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
                System.out.println("Shipment Incomes Total: " + store.getTotalShipmentIncome());
            }
        });
    }

    @Override
    public void displayProducts(List<Product> allProducts) {

    }

    @Override
    public void summarizeOrder(OrderInvoice orderInvoice) {

    }

    @Override
    public void displayError(String message) {

    }

    @Override
    public void fileLoadedSuccessfully() {

    }

    @Override
    public void showOrdersHistory(List<OrderInvoice> ordersHistory) {

    }

    // TODO: [View][Dudi]: Add view prompt for xml path
    @Override
    public String promptUserXmlFilePath() {
        return "/Users/noamwa/Desktop/Studies/java/ex02-sdp/MTA-JAVA_Super-Duper-Market/Model/src/resource/ex1-big.xml";
    }
}
