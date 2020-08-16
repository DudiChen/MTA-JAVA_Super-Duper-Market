package adapters;
import entities.Store;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IdToStoreAdapter extends XmlAdapter<IdToStoreAdapter.IdToStore, Map<Integer, Store>> {

    public static class IdToStore {
        public List<IdToStoreEntry> entries = new ArrayList<>();
    }

    public static class IdToStoreEntry {

        @XmlAttribute
        public int id;
        @XmlElement
        public Store value;

        public int getKey() {
            return id;
        }

        public Store getValue() {
            return value;
        }
    }

    @Override
    public Map<Integer, Store> unmarshal(IdToStore adaptedMap) throws ValidationException {

        StringBuilder errMsg = new StringBuilder();
        Stream<Store> storeStream = adaptedMap.entries.stream().map(entry -> entry.value);

        validateDuplicateIds(storeStream)
                .ifPresent(err -> errMsg.append(err));

        if (errMsg.toString().isEmpty()) {
            throw new ValidationException(errMsg.toString());
        } else {
            return adaptedMap.entries.stream()
                    .collect(Collectors.toMap(IdToStoreEntry::getKey, IdToStoreEntry::getValue));
        }
    }

    private Optional<String> validateDuplicateIds(Stream<Store> stores) {
        Stream<Integer> duplicateIDs = stores
                .map(store -> store.getId())
                .filter(id -> Collections.frequency(stores.collect(Collectors.toList()), id) > 1);
        if (duplicateIDs.findAny().isPresent()) {
            return Optional.of(duplicateIDs.map(Object::toString)
                    .reduce((acc, dupId) -> acc + "duplicate product id:" + dupId + System.lineSeparator())
                    .get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IdToStore marshal(Map<Integer, Store> v) {
        return null;
    }
}
