package view.menu;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import view.menu.item.MapElement;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A node that draws a triangle grid of dots using canvas
 */
public class MapMenu extends Pane implements Navigatable {
//    private static final double SPACING_X = 25;
//    private static final double SPACING_Y = 20;
    private static final double RADIUS = 1.5;
    private final List<MapElement> mapElements;
    private Canvas canvas = new Canvas();

    public MapMenu(List<MapElement> mapElements) {
        this.mapElements = mapElements;
        getChildren().add(canvas);
    }

    @Override protected void layoutChildren() {
        AtomicReference<Integer> maxYPoint = new AtomicReference<>(0);
        AtomicReference<Integer> maxXPoint = new AtomicReference<>(0);

        // calculating max points plus added frame
        (this.mapElements.stream()
                .mapToInt(mapElement -> (int) (mapElement.getPoint().getY())).max()).ifPresent(maxY -> maxYPoint.updateAndGet(v -> v + maxY + 3));
        (this.mapElements.stream()
                .mapToInt(mapElement -> (int) (mapElement.getPoint()).getX()).max()).ifPresent(maxX -> maxXPoint.updateAndGet(v -> v + maxX + 3));

//        // setting grid's size
//        mapGrid.setPrefColumns(maxXPoint.get());
//        mapGrid.setPrefRows(maxYPoint.get());
//        mapGrid.setMaxWidth(Region.USE_PREF_SIZE);
//
//
//        Stream<Integer> corners = Stream.of(
//                0, // upper left
//                maxXPoint.get() - 1, // upper right
//                (maxYPoint.get() - 1) * (maxXPoint.get()), // lower left
//                (maxYPoint.get() - 1) * (maxXPoint.get()) + maxXPoint.get() - 1 // lower right
//        );
//        IntStream upperParts = IntStream.range(0, maxXPoint.get() - 1)
//                .filter(index -> corners.anyMatch(Predicate.isEqual(index)));
//        IntStream lowerParts = IntStream.range(
//                (maxYPoint.get() - 1) * (maxXPoint.get()),
//                (maxYPoint.get() - 1) * (maxXPoint.get()) + maxXPoint.get() - 1
//        ).filter(index -> corners.anyMatch(Predicate.isEqual(index)));
//
//        IntStream sides = IntStream.concat(
//                IntStream.range(0, maxYPoint.get() - 1).map(x -> x * maxXPoint.get()),
//                IntStream.range(0, maxYPoint.get() - 1).map(x -> x * maxXPoint.get() + maxXPoint.get() - 1)
//        );

        final int top = (int)snappedTopInset();
        final int right = (int)snappedRightInset();
        final int bottom = (int)snappedBottomInset();
        final int left = (int)snappedLeftInset();
        final int w = (int)getWidth() - left - right;
        final int h = (int)getHeight() - top - bottom;

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            GraphicsContext g = canvas.getGraphicsContext2D();
            g.clearRect(0, 0, w, h);
            g.setFill(Color.gray(0,0.2));

            for (int x = 0; x < w; x += maxXPoint.get()) {
                for (int y = 0; y < h; y += maxYPoint.get()) {
                    Image image = new Image(getClass().getResourceAsStream("resource/store.png"));
                    g.drawImage(image, x, y);
                    double offsetY = (y%(2*maxYPoint.get())) == 0 ? maxYPoint.get() /2 : 0;
                    g.fillOval(x-RADIUS+offsetY,y-RADIUS,RADIUS+RADIUS,RADIUS+RADIUS);
                }
            }
        }
    }

    @Override
    public Node getContent() {
        return this;
    }
}