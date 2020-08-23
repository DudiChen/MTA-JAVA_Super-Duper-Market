package controller;

import com.sun.org.apache.xpath.internal.operations.String;
import com.sun.tools.javac.util.Pair;
import entity.Market;
import entity.Store;
import exception.OrderValidationException;
import view.View;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        view.displayStores(market.getAllStores());
        AtomicReference<Store> chosenStore = null;
        view.onStoreIdChoice = (storeId) -> {
            chosenStore.set(market.getStoreById(storeId));
            view.displayProducts(market.getAllProducts());
        }
        view.onOrderPlaced = (date, destination, productPricePair) -> {
            StringBuilder err = new StringBuilder();
            // validate store coordination is not the same as customer coordinate
            if (destination.equals(chosenStore.get().getCoordinate())) {
                err.append("cannot make order from same coordinate as store" + System.lineSeparator());
            }
            // validate chosen products are sold by the chosen store
            for (Pair<Integer, Integer> productToQuantity : productPricePair) {
                int productId = productToQuantity.fst;
                if (!chosenStore.get().isProductSold(productId)) {
                    err.append(market.getProductById(productId).toString() + " is not sold by " + market.getStoreById(chosenStore.get().getId()) + System.lineSeparator());
                }
            }
            if (err.length() > 0) {
                throw new OrderValidationException(err.toString());
            }
            // return order result
        };
    }
}
