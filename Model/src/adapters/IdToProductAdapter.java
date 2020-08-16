package adapters;
import entities.Product;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class IdToProductAdapter extends XmlAdapter<IdToProductAdapter.IdToProduct, Map<Integer, Product>> {

    public static class IdToProduct {
        public List<IdToProductEntry> entries = new ArrayList<>();
    }

    public static class IdToProductEntry {

        @XmlAttribute
        public int id;
        @XmlElement
        public Product value;

        public int getKey() {
            return id;
        }

        public Product getValue() {
            return value;
        }
    }

    @Override
    public Map<Integer, Product> unmarshal(IdToProduct adaptedMap) throws ValidationException {

        StringBuilder errMsg = new StringBuilder();
        Stream<Product> productStream = adaptedMap.entries.stream().map(entry -> entry.value);

        validateDuplicateIds(productStream)
                .ifPresent(err -> errMsg.append(err));

        if (errMsg.toString().isEmpty()) {
            throw new ValidationException(errMsg.toString());
        } else {
            return adaptedMap.entries.stream()
                    .collect(Collectors.toMap(IdToProductEntry::getKey, IdToProductEntry::getValue));
        }
    }


    private Optional<String> validateDuplicateIds(Stream<Product> products) {
        Stream<Integer> duplicateIDs = products
                .map(store -> store.getId())
                .filter(id -> Collections.frequency(products.collect(Collectors.toList()), id) > 1);
        if (duplicateIDs.findAny().isPresent()) {
            return Optional.of(duplicateIDs.map(Object::toString)
                    .reduce((acc, dupId) -> acc + "duplicate product id:" + dupId + System.lineSeparator())
                    .get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IdToProduct marshal(Map<Integer, Product> v) throws Exception {
        return null;
    }
}
