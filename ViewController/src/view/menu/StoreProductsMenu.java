package view.menu;

import controller.Controller;
import entity.Discount;
import entity.Product;
import entity.Store;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import view.TriConsumer;
import view.menu.item.DiscountContent;
import view.menu.item.ProductsContentFactory;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class StoreProductsMenu<T> extends ProductsMenu {

    private final Stage primaryStage;
    private final Store store;
    private final Controller controller;
    private Popup currentPopup;
    private boolean currentPopupFocused = true;
    private boolean hovered = true;

    public StoreProductsMenu(List<Product> products, Store store, TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced, Stage primaryStage, Controller controller) {
        super(products, new ProductsContentFactory(store));
        this.controller = controller;
        this.productsContentFactory.setOnHover(this::onProductHover);
        this.productsContentFactory.setOnUnHover(this::onProductUnHover);
        this.setOnOrderPlaced(onOrderPlaced);
        this.primaryStage = primaryStage;
        this.store = store;
    }

    public void onProductHover(Product product, Point mousePos) {
        if (this.currentPopup == null) {
            List<Discount> discounts = this.store.getDiscountsByProductId(product.getId());
            if (discounts != null) {
                DiscountsMenu discountsMenu = new DiscountsMenu(discounts, this.controller);
                discountsMenu.getContent().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                    onPopupUnHover();
                });
                discountsMenu.getContent().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    onPopupHover();
                });
                Popup popup = createPopup(discountsMenu);
                popup.show(primaryStage, this.primaryStage.getX() + mousePos.getX(), this.primaryStage.getY() + mousePos.getY() + 150);
            }
        }

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
}
