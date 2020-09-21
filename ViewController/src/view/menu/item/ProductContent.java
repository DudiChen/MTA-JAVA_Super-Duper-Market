package view.menu.item;

import entity.Product;
import entity.Store;
import entity.StoreProduct;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Collection;
import java.util.List;

public class ProductContent extends AbstractProductContent {

    @FXML
    private Label numberOfSellingStoresLabel;
    @FXML
    private Label averagePriceLabel;
    @FXML
    private Label numberOfTotalProductSalesLabel;
    private List<Store> allStores;

    @Override
    protected String getFXMLName() {
        return "product";
    }

    public ProductContent(List<Store> allStores) {
        super();
        this.allStores = allStores;
    }

    @Override
    protected void bind(Product product) {
        double averagePrice = allStores.stream()
                    .map(Store::getStock)
                    .map(stock -> stock.getSoldProducts().values())
                    .flatMap(Collection::stream)
                    .filter(storeProduct -> storeProduct.getId() == product.getId())
                    .map(StoreProduct::getPrice)
                    .reduce((double) 0, (acc, curr) -> acc += curr);
        this.averagePriceLabel.setText("Average Price: " + String.format("%.2f", averagePrice));
        int numberOfSellingStores = (int) allStores.stream()
                    .filter(store -> store.isProductSold(product.getId())).count();
        this.numberOfSellingStoresLabel.setText( "Total Selling Stores: " + numberOfSellingStores);
        int numberOfTotalProductSales = allStores.stream()
                    .map(store -> store.getTotalProductSales(product.getId()))
                    .reduce(0, (acc, curr) -> acc += curr);
        this.numberOfTotalProductSalesLabel.setText("Total Sales: " + numberOfTotalProductSales);
    }
}
