package view.menu.item;

import entity.Store;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

import java.awt.*;
import java.util.function.Consumer;


public class StoreMapElement implements MapElement {
    private final Store store;
    private Consumer<Integer> onStoreIdChoice;
    private Popup parent;

    public StoreMapElement(Store store, Consumer<Integer> onStoreIdChoice) {
        this.store = store;
        this.onStoreIdChoice = onStoreIdChoice;
    }

    @Override
    public Point getPoint() {
        return this.store.getLocation();
    }

    @Override
    public String getIconName() {
        return "store.png";
    }

    @Override
    public Popup getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Popup parent) {
        this.parent = parent;
    }

    @Override
    public Node getContent() {
        ListView<Store> storeContainer = new ListView<>();
        storeContainer.setCellFactory(param -> new StoreContent((storeId) -> {
            this.parent.hide();
            this.onStoreIdChoice.accept(storeId);
        }));
        storeContainer.getItems().add(this.store);
        return storeContainer;
    }
}
