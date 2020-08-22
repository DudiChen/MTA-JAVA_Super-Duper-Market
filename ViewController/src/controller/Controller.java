package controller;

import entity.Market;
import entity.Store;

import java.util.List;

public class Controller {
    Market market = new Market();

    public void fetchAllStoresToUI() {
        List<Store> stores = market.getAllStores();

    }

    public void loadXMLData() {

    }

    public void performOrder() {
    }

    public void addNewProduct() {
    }

    public void fetchAllProductsToUI() {
    }

    public void getCustomerToUI() {
    }
}
