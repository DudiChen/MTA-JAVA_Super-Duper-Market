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

    Map<Integer, Product> idToProduct;
    Map<Integer, Store> idToStore;

    @Override
    public Market build(SuperDuperMarketDescriptor source) throws ValidationException {
        idToProduct = getIdToProduct(source.getSDMItems().getSDMItem());
        idToStore = getIdToStore(source.getSDMStores().getSDMStore(), idToProduct);
        return new Market(idToStore, idToProduct);
    }

    private Map<Integer, Product> constructIdToProduct(Set<SDMItem> sdmItems) {
        return sdmItems.stream().collect(Collectors.toMap(SDMItem::getId, item -> new ProductBuilder().build(item)));
    }

    private Map<Integer, Store> constructIdToStore(Set<SDMStore> sdmStores) {
        return sdmStores.stream().collect(Collectors.toMap(SDMStore::getId, store -> new StoreBuilder(idToProduct).build(store)));
    }

    private Set<Integer> getDuplicateIds(List<Integer> ids) {
        Set<Integer> allItems = new HashSet<>();
        return ids.stream()
                .filter(n -> !allItems.add(n))
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
        StringBuilder errors = new StringBuilder();

        // check for duplicate ids stores
        errors.append(dups.stream().map(Object::toString)
                .reduce("", (acc, current) -> acc + "duplicate id for store " + current.toString() + System.lineSeparator()));


        Map<Integer, Store> idToStore = constructIdToStore(new HashSet<>(sdmStores));

        // check for non existing products sold by stores
        Map<Integer, List<Product>> nonValidStoresToNonExistingProds = getNonExistingProducts(new HashSet<>(idToStore.values()), idToProduct);
        nonValidStoresToNonExistingProds
                .forEach((store, products) -> {
                    if (products.size() > 0) {
                        products
                                .forEach(product -> errors.append("product" + product.toString() + " sold by " + store.toString() + "but doesn't exist" + System.lineSeparator()));
                    }
                });

        // check for duplicate product sell
        Map<Integer, List<Product>> nonValidStoreIdsToDuplicateProds = getDuplicateProducts(new HashSet<>(idToStore.values()), idToProduct);
        nonValidStoreIdsToDuplicateProds
                .forEach((storeId, products) -> {
                    if (products.size() > 0) {
                        products
                                .forEach(product -> errors.append("product" + product.toString() + " sold by " + storeId.toString() + "more then once" + System.lineSeparator()));
                    }
                });


        // 3.5 validation
        List<Product> nonSoldProducts = getNonSoldProducts(new HashSet<>(idToStore.values()), new HashSet<>(idToProduct.values()));
        errors.append(nonSoldProducts.stream().map(Product::getId).map(Object::toString)
                .reduce("", (acc, currProductId) -> acc + "item id " + currProductId + " is not sold by any store" + System.lineSeparator()));


        if (errors.length() > 0) {
            throw new ValidationException(errors.toString());
        }
        return idToStore;
    }

    private List<Product> getNonSoldProducts(Set<Store> idToStore, Set<Product> idToProduct) {
        List<Product> unSoldProducts = new ArrayList<>();
        for (Product product : idToProduct) {
            boolean isSold = false;
            for (Store store : idToStore) {
                if (store.getStock().doesProductExist(product)) {
                    isSold = true;
                    break;
                }
            }
            if (!isSold) {
                unSoldProducts.add(product);
            }
        }
        return unSoldProducts;
    }

    private Map<Integer, List<Product>> getDuplicateProducts(Set<Store> stores, Map<Integer, Product> idToProduct) {
        Map<Integer, List<Product>> res = new HashMap<>();
        stores.stream()
                .forEach(store ->
                        res.put(store.getId(),
                                store.getStock().getSoldProduts().values()
                                        .stream()
                                        .map(storeProduct -> storeProduct.getProduct())
                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                        .entrySet().stream()
                                        .filter(idToApearances -> idToApearances.getValue() > 1)
                                        .map(Map.Entry::getKey)
                                        .map(id -> idToProduct.get(id))
                                        .collect(Collectors.toList())
                        ));
        return res;
    }

    private Map<Integer, List<Product>> getNonExistingProducts(Set<Store> stores, Map<Integer, Product> idToProduct) {
        Map<Integer, List<Product>> res = new HashMap<>();
        stores.stream()
                .forEach(store -> {
                            res.put(store.getId(), store.getStock().getSoldProduts()
                                    .values()
                                    .stream()
                                    .map(StoreProduct::getProduct)
                                    .filter(storeProduct -> !idToProduct.containsKey(storeProduct.getId()))
                                    .collect(Collectors.toList()));
                        }
                );
        return res;
    }
}
