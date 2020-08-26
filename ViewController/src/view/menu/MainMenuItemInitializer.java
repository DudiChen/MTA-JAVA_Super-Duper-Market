package view.menu;

import command.LoadFromXMLCommand;
import command.customer.MakeDynamicOrderCommand;
import command.customer.MakeOrderCommand;
import command.store.*;
import controller.Controller;
import main.Main;

public class MainMenuItemInitializer {
    public static MainMenu initialize(Controller controller) {
        MenuItemNonActionable mainMenuItem = new MenuItemNonActionable("Main Menu");
        MenuItemActionable loadXmlMenuItem = new MenuItemActionable(controller, "Load XML Data File");
        MenuItemActionable showAllStoresMenuItem = new MenuItemActionable(controller,"Show All Stores Information");
        MenuItemActionable showAllProductsMenuItem = new MenuItemActionable(controller,"Show All Products Information");
        MenuItemActionable placeOrderMenuItem = new MenuItemActionable(controller,"Place an Order From a Store");
        MenuItemActionable placeDynamicOrderMenuItem = new MenuItemActionable(controller,"Let Us Order The Products For You");
        MenuItemActionable showOrdersHistoryMenuItem = new MenuItemActionable(controller,"Show Orders History");
        MenuItemActionable saveHistoryMenuItem = new MenuItemActionable(controller, "Save Orders History");
        MenuItemActionable loadHistoryMenuItem = new MenuItemActionable(controller, "Load Orders History");

        loadXmlMenuItem.setOnSelectedCommand(new LoadFromXMLCommand());
        showAllStoresMenuItem.setOnSelectedCommand(new GetAllStoresCommand());
        showAllProductsMenuItem.setOnSelectedCommand(new GetAllProductsCommand());
        placeOrderMenuItem.setOnSelectedCommand(new MakeOrderCommand());
        placeDynamicOrderMenuItem.setOnSelectedCommand(new MakeDynamicOrderCommand());
        showOrdersHistoryMenuItem.setOnSelectedCommand(new ShowOrdersHistoryCommand());
        saveHistoryMenuItem.setOnSelectedCommand(new SaveOrdersHistoryCommand());
        loadHistoryMenuItem.setOnSelectedCommand(new LoadOrdersHistoryCommand());

        mainMenuItem.addSubMenuItem(loadXmlMenuItem);
        mainMenuItem.addSubMenuItem(showAllStoresMenuItem);
        mainMenuItem.addSubMenuItem(showAllProductsMenuItem);
        mainMenuItem.addSubMenuItem(placeOrderMenuItem);
        mainMenuItem.addSubMenuItem(placeDynamicOrderMenuItem);
        mainMenuItem.addSubMenuItem(showOrdersHistoryMenuItem);
        mainMenuItem.addSubMenuItem(saveHistoryMenuItem);
        mainMenuItem.addSubMenuItem(loadHistoryMenuItem);

//        dateTimeMenuItem.addSubMenuItem(dateActionMenuItem);
//        dateTimeMenuItem.addSubMenuItem(timeActionMenuItem);
//        versionAndDigitsMenuItem.addSubMenuItem(versionActionMenuItem);
//        versionAndDigitsMenuItem.addSubMenuItem(spaceCounterActionMenuItem);

        return new MainMenu(mainMenuItem);
    }
}
