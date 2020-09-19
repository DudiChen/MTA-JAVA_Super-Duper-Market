package view.menu.item;
import javafx.scene.Group;
import view.menu.Navigatable;

import java.awt.Point;

public interface MapElement extends Navigatable {
    Point getPoint();
    String getIconName();
}
