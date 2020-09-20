package view.menu.item;
import javafx.scene.Group;
import javafx.stage.Popup;
import view.menu.Navigatable;

import java.awt.Point;

public interface MapElement extends Navigatable {
    Point getPoint();
    String getIconName();
    Popup getParent();
    void setParent(Popup parent);
}
