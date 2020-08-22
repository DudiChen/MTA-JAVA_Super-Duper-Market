package builder;

import java.awt.*;
import jaxb.generated.Location;

import javax.xml.bind.ValidationException;

public class PointBuilder implements Builder<Location, Point> {
    @Override
    public Point build(Location xmlLocation) {
        return new Point(xmlLocation.getX(), xmlLocation.getY());
    }
}
