package adapters;

import entities.Coordinate;
import jaxb.generated.Location;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CoordinateAdapter extends XmlAdapter<Location, Coordinate> {

    @Override
    public Coordinate unmarshal(Location location) throws ValidationException {
        if(location.getX() < 0 || location.getY() < 0 || location.getX() > 50 || location.getY() > 50) {
            throw new ValidationException("coordinates must be between 0 to 50" + System.lineSeparator());
        }
        return new Coordinate(location.getX(), location.getY());
    }

    @Override
    public Location marshal(Coordinate v) throws Exception {
        return null;
    }
}
