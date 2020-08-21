package builder;

import entity.Market;
import entity.Product;
import entity.Store;
import entity.StoreProduct;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMStore;
import jaxb.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MarketBuilder implements Builder<SuperDuperMarketDescriptor, Market> {

    @Override
    public Market build(SuperDuperMarketDescriptor source) throws ValidationException {
        Map<Integer, Product> idToProduct = getIdToProduct(source.getSDMItems().getSDMItem());
        Map<Integer, Store> idToStore = getIdToStore(source.getSDMStores().getSDMStore(), idToProduct);
        return new Market(idToStore, idToProduct);
    }

    private Map<Integer, Product> constructIdToProduct(Set<SDMItem> sdmItems) {
        return sdmItems.stream().collect(Collectors.toMap(SDMItem::getId, item -> new ProductBuilder().build(item)));
    }

    private Map<Integer, Store> constructIdToStore(Set<SDMStore> sdmStores) {
        return sdmStores.stream().collect(Collectors.toMap(SDMStore::getId, store -> new StoreBuilder().build(store)));
    }

    private Set<Integer> getDuplicateIds(List<Integer> ids) {
        Set<Integer> allItems = new HashSet<>();
        return ids.stream()
                .filter(n -> !allItems.add(n)) //Set.add() returns false if the item was already in the set.
                .collect(Collectors.toSet());
    }

    private Map<Integer, Product> getIdToProduct(List<SDMItem> sdmItems) throws ValidationException {
        List<Integer> ids = sdmItems.stream().map(SDMItem::getId).collect(Collectors.toList());
        Set<Integer> dups = getDuplicateIds(ids);
        if (dups.size() > 0) {
            String dupIdErrors = dups.stream().map(Object::toString)
                    .reduce("", (acc, current) -> acc + "duplicate id for item " + current.toString() + System.lineSeparator());
            throw new ValidationException(dupIdErrors);
        } else {
            return constructIdToProduct(new HashSet<>(sdmItems));
        }
    }

    private Map<Integer, Store> getIdToStore(List<SDMStore> sdmStores, Map<Integer, Product> idToProduct) throws ValidationException {
        List<Integer> ids = sdmStores.stream().map(SDMStore::getId).collect(Collectors.toList());
        Set<Integer> dups = getDuplicateIds(ids);

        // check for duplicate ids stores
        if (dups.size() > 0) {
            String dupIdErrors = dups.stream().map(Object::toString)
                    .reduce("", (acc, current) -> acc + "duplicate id for store " + current.toString() + System.lineSeparator());
            throw new ValidationException(dupIdErrors);
        }

        // check for non existing products sold by stores
        Map<Store, List<Product>> nonValidStoresToNonExistingProds = getNonExistingProducts(new HashSet<>(sdmStores)
                , idToProduct);
        if (nonValidStoresToNonExistingProds.size() > 0) {
            StringBuilder errors = new StringBuilder();
            nonValidStoresToNonExistingProds
                    .forEach((store, products) -> {
                        if (products.size() > 0) {
                            products
                                    .forEach(product -> errors.append("product" + product.toString() + " sold by " + store.toString() + "but doesn't exist" + System.lineSeparator()));
                        }
                    });
            throw new ValidationException(errors.toString());
        }
        // check for unsold products
        Map<Integer, Store> idToStore = constructIdToStore(new HashSet<>(sdmStores));
        List<Product> nonSoldProducts = getNonSoldProducts(idToStore, idToProduct);
        if (nonValidStoresToNonExistingProds.size() > 0) {
            String errors = nonSoldProducts.stream().map(Product::getId).map(Object::toString)
                    .reduce("", (acc, currProductId) -> acc + "item id " + currProductId + "is not sold by any store" + System.lineSeparator());
            throw new ValidationException(errors);
        }

        // check for duplicate product sell
        Map<Store, List<Product>> nonValidStoresToDuplicateProds = getDuplicateProducts(new HashSet<>(sdmStores), idToProduct);
        if (nonValidStoresToDuplicateProds.size() > 0) {
            StringBuilder errors = new StringBuilder();
            nonValidStoresToDuplicateProds
                    .forEach((store, products) -> {
                        if (products.size() > 0) {
                            products
                                    .forEach(product -> errors.append("product" + product.toString() + " sold by " + store.toString() + "more then once" + System.lineSeparator()));
                        }
                    });
            throw new ValidationException(errors.toString());
        }
        return idToStore;
    }


    private List<Product> getNonSoldProducts(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct) {
        return idToProduct.values().stream()
                .filter(product ->
                        idToStore.values().stream()
                                .anyMatch(store -> store.getStock().doesProductExist(product)))
                .collect(Collectors.toList());
    }

    private Map<Store, List<Product>> getDuplicateProducts(Set<SDMStore> sdmStores, Map<Integer, Product> idToProduct) {
        Map<Store, List<Product>> res = new HashMap<>();
        sdmStores.stream()
                .forEach(store ->
                        res.put(new StoreBuilder().build(store),
                                store.getSDMPrices().getSDMSell()
                                .stream()
                                .map(sdmSell -> idToProduct.get(sdmSell.getItemId()).getId())
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                .entrySet().stream()
                                .filter(idToApearances -> idToApearances.getValue() > 1)
                                .map(Map.Entry::getKey)
                                .map(id -> idToProduct.get(id))
                                .collect(Collectors.toList())
                ));
        return res;
    }

    private Map<Store, List<Product>> getNonExistingProducts(Set<SDMStore> sdmStores, Map<Integer, Product> idToProduct) {
        Map<Store, List<Product>> res = new HashMap<>();
        sdmStores.stream()
                .map(sdmStore -> new StoreBuilder().build(sdmStore))
                .forEach(store ->
                        res.put(store,
                                store.getStock().getSoldProduts()
                                        .values()
                                        .stream()
                                        .filter(storeProduct -> !idToProduct.containsKey(storeProduct.getProduct().getId()))
                                        .map(StoreProduct::getProduct)
                                        .collect(Collectors.toList()))
                );
        return res;
    }
}
