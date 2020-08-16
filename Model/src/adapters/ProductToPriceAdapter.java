package adapters;

import entities.Product;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductToPriceAdapter extends XmlAdapter<ProductToPriceAdapter.AdaptedMap, Map<Product, Integer>> {

    public static class AdaptedMap {
        public List<Entry> entries = new ArrayList<>();
    }

    public static class Entry {
        @XmlElement
        public Product key;
        @XmlAttribute
        public int value;

        public Product getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    public Map<Product, Integer> unmarshal(AdaptedMap adaptedMap) throws ValidationException {

        StringBuilder errMsg = new StringBuilder();
        Stream<Product> productStream = adaptedMap.entries.stream().map(entry -> entry.key);


        checkForDuplicateProducts(productStream)
                .ifPresent(err -> errMsg.append((err)));

        return adaptedMap.entries.stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public AdaptedMap marshal(Map<Product, Integer> v) throws Exception {
        return null;
    }

    private Optional<String> checkForDuplicateProducts(Stream<Product> products) {
        String err = String.join(System.lineSeparator(),
                products
                        .filter(prod -> Collections.frequency(products.collect(Collectors.toList()), prod) > 1)
                        .map(dupProd -> "product id: " + dupProd.getId() + " is duplicated").collect(Collectors.toList()));

        if (err.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(err);
        }
    }
}
