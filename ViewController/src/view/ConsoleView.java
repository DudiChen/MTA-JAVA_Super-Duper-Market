package view;

import controller.Controller;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
import view.menu.MainMenu;
import view.menu.MainMenuItemInitializer;

import java.util.List;

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
