package view.menu;

import controller.Controller;
import entity.Discount;
import entity.Product;
import entity.Store;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;
import view.ApplicationContext;
import view.TriConsumer;
import view.menu.item.ProductsContentFactory;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StoreProductsMenu extends ProductsMenu {

    private final Stage primaryStage;
    private final Store store;
    private Popup currentPopup;
    private boolean currentPopupFocused = true;
    private boolean hovered = true;
    private List<Discount> chosenDiscounts;

    @FXML
    private Label storeNameLabel;
    @FXML
    private Label chosenDiscountsLabel;
    @FXML
    private ComboBox<String> newProductBox;

    public StoreProductsMenu(List<Product> products, Store store, TriConsumer<Date, Integer, Pair<List<Pair<Integer, Double>>, List<Discount.Offer>>> onOrderPlaced, Stage primaryStage, Controller controller) {
        super(products, new ProductsContentFactory(store), controller);
        this.productsContentFactory.setOnDelete(this::onProductDelete);
        this.productsContentFactory.setOnPriceChange(this::onProductPriceChange);
        this.productsContentFactory.setOnHover(this::onProductHover);
        this.productsContentFactory.setOnUnHover(this::onProductUnHover);
        this.setOnOrderPlaced(onOrderPlaced);
        this.primaryStage = primaryStage;
        this.chosenDiscounts = new ArrayList<>();
        this.store = store;
        this.storeNameLabel.setText(store.getName());
    }

    public void onProductHover(Product product, Point mousePos) {
        if (this.currentPopup == null) {
            List<Discount> discounts = this.store.getDiscountsByProductId(product.getId());
            if (discounts != null && !discounts.isEmpty()) {
                DiscountsMenu discountsMenu = new DiscountsMenu(discounts, this.controller, this::onDiscountChoice);
                discountsMenu.getContent().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                    onPopupUnHover();
                });
                discountsMenu.getContent().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    onPopupHover();
                });
                Popup popup = createPopup(discountsMenu);
                popup.show(primaryStage, this.primaryStage.getX() + mousePos.getX(), this.primaryStage.getY() + mousePos.getY() + 180);
            }
        }
    }

    // TODO:: look here
    private void onDiscountChoice(Discount discount, List<Discount.Offer> offers) {
        if (!this.isAvailableDiscount(discount)) {
            return;
        }
        this.chosenDiscountsLabel.setText(this.chosenDiscountsLabel.getText() + ", " + discount.getName());
        this.chosenOffers.addAll(offers);
        this.chosenDiscounts.add(discount);
    }

    private boolean isAvailableDiscount(Discount discount) {
        this.getOrderDetails();
        return controller.isAvailableDiscount(discount, this.orderProducts, this.chosenDiscounts);
    }

    public void onProductUnHover(Product product, Point mousePos) {
        if (currentPopup != null) {
            closePopup();
        }
    }

    private void onPopupHover() {

    }

    private void onPopupUnHover() {
        if (currentPopup != null) {
            closePopup();
        }
    }

    private void closePopup() {
        this.currentPopup.hide();
        this.currentPopup = null;
    }

    private Popup createPopup(DiscountsMenu discountsMenu) {
        StackPane content = new StackPane(discountsMenu.getContent());
        content.setPadding(new Insets(10, 5, 10, 5));
        content.setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
        content.setEffect(new DropShadow());
        Popup popup = new Popup();
        popup.getContent().add(content);
        this.currentPopup = popup;
        return popup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        List<Product> productsThatAreNotSoldByThisStore = this.controller.getAllProducts().stream().filter(product -> !this.products.contains(product)).collect(Collectors.toList());
        this.newProductBox.getItems().addAll(productsThatAreNotSoldByThisStore.stream().map(product -> product.getId() + ": " + product.getName()).collect(Collectors.toList()));
        this.newProductBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            ApplicationContext.getInstance().navigateBack();
            this.onProductAdd(Integer.parseInt(newVal.split(":")[0]));
        });
    }

    @Override
    protected boolean validateOrder() {
        boolean isValidForProducts = super.validateOrder();
        boolean isValidForDiscounts = true;
        List<Discount> validDiscounts = new ArrayList<>();
        List<Discount> nonValidDiscounts = new ArrayList<>();
        List<Discount> temp = new ArrayList<>();
        temp.addAll(this.chosenDiscounts);
        for(int i = 0; i  < temp.size(); i++) {
            this.chosenDiscounts = temp.subList(0, i);
            if(isAvailableDiscount(temp.get(i))) {
                validDiscounts.add(temp.get(i));
            }
            else {
                nonValidDiscounts.add(temp.get(i));
            }
        }
        this.chosenDiscounts = temp;
        String removedDiscounts = nonValidDiscounts.stream()
                .map(Discount::getName)
                .reduce("", (acc, curr) -> acc + " " + curr);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (validDiscounts.size() != this.chosenDiscounts.size()) {
            alert.setContentText("Removed Discounts: " + removedDiscounts);
            this.chosenDiscounts = validDiscounts;
            this.chosenDiscountsLabel.setText("Chosen Discounts: " +
                    this.chosenDiscounts.stream().map(Discount::getName).reduce("", (acc, curr) ->  acc + ", " + curr));
            isValidForDiscounts = false;
            alert.show();
        }
        return isValidForDiscounts && isValidForProducts;
    }


    private void onProductDelete(int productId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Delete Product");
        alert.setHeaderText("This will delete " + controller.getProductById(productId).getName() + " for " + store.getName());
        alert.setContentText("Are you sure ?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(buttonType -> {
                    Stream<Product> productStream = this.products.stream();
                    this.products = productStream.filter(product -> product.getId() != productId).collect(Collectors.toList());
//                    try {
                    controller.deleteProduct(productId, store.getId());
//                    }  catch(ValidationException e){

//                    }
                });
    }

    private void onProductPriceChange(int productId, double newPrice) {
        controller.changePriceForProduct(store.getId(), productId, newPrice);
    }

    private void onProductAdd(int productId) {
        this.controller.addNewProduct(store.getId(), productId);
    }
}
