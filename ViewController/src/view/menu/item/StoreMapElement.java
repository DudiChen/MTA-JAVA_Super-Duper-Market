package view.menu.item;

import entity.Store;
import javafx.scene.Group;

import java.awt.*;

public class StoreMapElement implements MapElement {
    private Point point;
    public StoreMapElement(Store store) {
        this.point = store.getCoordinate();
    }

    @Override
    public Point getPoint() {
        return this.point;
    }

    @Override
    public String getIconName() {
        return "store.png";
    }

}
