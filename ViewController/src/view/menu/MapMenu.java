package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.Pair;
import view.menu.item.MapElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class MapMenu extends StackPane implements Navigatable {

    private final List<MapElement> mapElements;
    private Canvas canvas;
    private List<Pair<Rectangle2D, Popup>> popups;


    public MapMenu(List<MapElement> mapElements) {
        this.popups = new ArrayList<>();
        this.mapElements = mapElements;
        canvas = new Canvas(getWidth(), getHeight());
        getChildren().add(canvas);
        widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.intValue()));
        heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.intValue()));
        canvas.setOnMouseMoved(
                e -> {
                    for (Pair<Rectangle2D, Popup> pair : this.popups) {
                        Rectangle2D bounds = pair.getKey();
                        Popup popup = pair.getValue();
                        // test if local mouse coordinates are within rectangle
                        if (bounds.contains(e.getX(), e.getY())) {
                            // convert local coordinates to screen coordinates
                            Point2D point = canvas.localToScreen(e.getX(), e.getY());
                            // show the popup at the mouse's location on the screen
                            popup.show(canvas, point.getX(), point.getY());
                        } else {
                            // hide popup if showing and mouse no longer within rectangle
                            popup.hide();
                        }
                    }
                });
    }

    private Popup createPopup(MapElement mapElement) {
        StackPane content = new StackPane(mapElement.getContent());
        content.setPadding(new Insets(10, 5, 10, 5));
        content.setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
        content.setEffect(new DropShadow());
        Popup popup = new Popup();
        popup.getContent().add(content);
        mapElement.setParent(popup);
        return popup;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int width = (int) getWidth() - left - right;
        final int height = (int) getHeight() - top - bottom;
        AtomicReference<Integer> maxYPoint = new AtomicReference<>(0);
        AtomicReference<Integer> maxXPoint = new AtomicReference<>(0);

        // calculating max points plus added frame
        (this.mapElements.stream()
                .mapToInt(mapElement -> (int) (mapElement.getPoint().getY())).max()).ifPresent(maxY -> maxYPoint.updateAndGet(v -> v + maxY + 3));
        (this.mapElements.stream()
                .mapToInt(mapElement -> (int) (mapElement.getPoint()).getX()).max()).ifPresent(maxX -> maxXPoint.updateAndGet(v -> v + maxX + 3));


        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(1); // change the line width

        final int hSpacing = (int) (canvas.getWidth() / maxXPoint.get());
        final int vSpacing = (int) (canvas.getHeight() / maxYPoint.get());
        final int hLineCount = (int) Math.floor((height + 1) / hSpacing);
        final int vLineCount = (int) Math.floor((width + 1) / vSpacing);

        gc.setStroke(Color.RED);
        for (int i = 0; i < hLineCount; i++) {
            gc.strokeLine(0, snap((i + 1) * hSpacing), width, snap((i + 1) * hSpacing));
        }

        gc.setStroke(Color.BLUE);
        for (int i = 0; i < vLineCount; i++) {
            gc.strokeLine(snap((i + 1) * vSpacing), 0, snap((i + 1) * vSpacing), height);
        }

        for (MapElement mapElement : mapElements) {
            Image image = new Image(getClass().getResourceAsStream("resource/" + mapElement.getIconName()), hSpacing + 20, vSpacing + 20, true, true);
            int x = (int) (hSpacing * mapElement.getPoint().getX()) + 1;
            int y = (int) (vSpacing * mapElement.getPoint().getY()) + 1;
            gc.drawImage(image, x, y);
            Rectangle2D bounds = new Rectangle2D(x, y, hSpacing + 20, vSpacing + 20);
            Popup popup = createPopup(mapElement);
            mapElement.setParent(popup);
            this.popups.add(new Pair<>(bounds, popup));
        }
    }

    private double snap(double y) {
        return ((int) y) + 0.5;
    }

    @Override
    public Node getContent() {
        return this;
    }
}