package controller;

import entity.Market;
import entity.Purchase;
import entity.Store;
import view.View;

import java.util.List;

public class Controller {
    Market market = new Market();
    View view;
    public void fetchAllStoresToUI() {
        List<Store> stores = market.getAllStores();

    }

    public void loadXMLData() {
    }

    public void addNewProduct() {
    }

    public void fetchAllProductsToUI() {
    }

    public void getCustomerToUI() {
    }

    public void makeOrder() {
        int storeId = view.getChosenStore(market.getAllStores());

        view.getOrderFromUser();
        while(){

        }
    }
}
