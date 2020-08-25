package view.menu;

import command.LoadFromXMLCommand;
import command.customer.MakeOrderCommand;
import command.store.GetAllProductsCommand;
import command.store.GetAllStoresCommand;
import command.store.ShowOrdersHistoryCommand;
import controller.Controller;
import main.Main;

public class MainMenuItemInitializer {
    public static MainMenu initialize(Controller controller) {
        MenuItemNonActionable mainMenuItem = new MenuItemNonActionable("Main Menu");
        MenuItemActionable loadXmlMenuItem = new MenuItemActionable(controller, "Load XML Data File");
        MenuItemActionable showAllStoresMenuItem = new MenuItemActionable(controller,"Show All Stores Information");
        MenuItemActionable showAllProductsMenuItem = new MenuItemActionable(controller,"Show All Products Information");
        MenuItemActionable placeOrderMenuItem = new MenuItemActionable(controller,"Place an Order");
        MenuItemActionable showOrdersHistoryMenuItem = new MenuItemActionable(controller,"Show Orders History");

        loadXmlMenuItem.setOnSelectedCommand(new LoadFromXMLCommand());
        showAllStoresMenuItem.setOnSelectedCommand(new GetAllStoresCommand());
        showAllProductsMenuItem.setOnSelectedCommand(new GetAllProductsCommand());
        placeOrderMenuItem.setOnSelectedCommand(new MakeOrderCommand());
        showOrdersHistoryMenuItem.setOnSelectedCommand(new ShowOrdersHistoryCommand());

        mainMenuItem.addSubMenuItem(loadXmlMenuItem);
        mainMenuItem.addSubMenuItem(showAllStoresMenuItem);
        mainMenuItem.addSubMenuItem(showAllProductsMenuItem);
        mainMenuItem.addSubMenuItem(placeOrderMenuItem);
        mainMenuItem.addSubMenuItem(showOrdersHistoryMenuItem);
//        dateTimeMenuItem.addSubMenuItem(dateActionMenuItem);
//        dateTimeMenuItem.addSubMenuItem(timeActionMenuItem);
//        versionAndDigitsMenuItem.addSubMenuItem(versionActionMenuItem);
//        versionAndDigitsMenuItem.addSubMenuItem(spaceCounterActionMenuItem);

        return new MainMenu(mainMenuItem);
    }
}
