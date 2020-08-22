package builder;

import entity.Market;
import entity.Product;
import entity.Store;
import entity.StoreProduct;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMSell;
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
        idToStore = getIdToStore(source.getSDMStores().getSDMStore());
        return new Market(idToStore, idToProduct);
    }

    private Map<Integer, Product> getIdToProduct(List<SDMItem> sdmItems) throws ValidationException {
        List<Integer> ids = sdmItems.stream().map(SDMItem::getId).collect(Collectors.toList());
        Set<Integer> dupIds = findDuplicates(ids);
        if (dupIds.size() > 0) {
            String dupIdErrors = dupIds.stream().map(Object::toString)
                    .reduce("", (acc, current) -> acc + "duplicate id for item " + current.toString() + System.lineSeparator());
            throw new ValidationException(dupIdErrors);
        } else {
            return constructIdToProduct(new HashSet<>(sdmItems));
        }
    }

    private Map<Integer, Store> getIdToStore(List<SDMStore> sdmStores) throws ValidationException {

        List<Integer> ids = sdmStores.stream().map(SDMStore::getId).collect(Collectors.toList());
        Set<Integer> dupIds = findDuplicates(ids);
        StringBuilder errors = new StringBuilder();


        // check for duplicate product sell of each store
        Map<Integer, List<Integer>> nonValidStoreIdsToDuplicateProds = getDuplicateProducts(sdmStores);
        nonValidStoreIdsToDuplicateProds
                .forEach((storeId, products) -> {
                    if (products.size() > 0) {
                        products
                                .forEach(product -> errors.append("product " + product.toString() + " sold by " + storeId.toString() + " more then once" + System.lineSeparator()));
                    }
                });
        // check for duplicate ids stores
        errors.append(dupIds.stream().map(Object::toString)
                .reduce("", (acc, current) -> acc + "duplicate id for store " + current.toString() + System.lineSeparator()));


        // check for non existing products sold by stores
        Map<Integer, List<Integer>> nonValidStoresToNonExistingProds = getNonExistingProducts(new HashSet<>(sdmStores));
        nonValidStoresToNonExistingProds
                .forEach((store, products) -> {
                    if (products.size() > 0) {
                        products
                                .forEach(productId -> errors.append("product " + productId.toString() + " sold by " + store.toString() + " but doesn't exist" + System.lineSeparator()));
                    }
                });


        // 3.5 validation - all the products are sold by at least one store
        List<Product> nonSoldProducts = getNonSoldProducts(new HashSet<>(sdmStores));
        errors.append(nonSoldProducts.stream().map(Product::getId).map(Object::toString)
                .reduce("", (acc, currProductId) -> acc + "item id " + currProductId + " is not sold by any store" + System.lineSeparator()));


        if (errors.length() > 0) {
            throw new ValidationException(errors.toString());
        }
        Map<Integer, Store> idToStore = constructIdToStore(new HashSet<>(sdmStores));
        return idToStore;
    }

    private Map<Integer, Product> constructIdToProduct(Set<SDMItem> sdmItems) {
        return sdmItems.stream().collect(Collectors.toMap(SDMItem::getId, item -> new ProductBuilder().build(item)));
    }

    private Map<Integer, Store> constructIdToStore(Set<SDMStore> sdmStores) {
        return sdmStores.stream().collect(Collectors.toMap(SDMStore::getId, store -> new StoreBuilder(idToProduct).build(store)));
    }

    private List<Product> getNonSoldProducts(Set<SDMStore> sdmStores) {
        List<Product> unSoldProducts = new ArrayList<>();
        for (Product product : new HashSet<>(idToProduct.values())) {
            boolean isSold = false;
            for (SDMStore sdmStore : sdmStores) {
                if (sdmStore.getSDMPrices().getSDMSell().stream().anyMatch(sell -> sell.getItemId() == product.getId())){
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

    private Map<Integer, List<Integer>> getDuplicateProducts(List<SDMStore> stores) {
        Map<Integer, List<Integer>> res = new HashMap<>();
        for (SDMStore sdmStore : stores) {
            Set<Integer> duplicateProductIds = findDuplicates(sdmStore.getSDMPrices().getSDMSell().stream().map(SDMSell::getItemId).collect(Collectors.toList()));
            if (duplicateProductIds.size() > 0) {
                res.put(sdmStore.getId(), duplicateProductIds.stream().collect(Collectors.toList()));
            }
        }
        return res;
    }

    private Map<Integer, List<Integer>> getNonExistingProducts(Set<SDMStore> stores) {
        Map<Integer, List<Integer>> res = new HashMap<>();

        for (SDMStore sdmStore : stores) {
            Set<Integer> nonExistingProdIds = sdmStore.getSDMPrices().getSDMSell().stream()
                    .map(sell -> sell.getItemId())
                    .filter(id -> !idToProduct.containsKey(id))
                    .collect(Collectors.toSet());
            if (nonExistingProdIds.size() > 0) {
                res.put(sdmStore.getId(), nonExistingProdIds.stream().collect(Collectors.toList()));
            }
        }
        return res;
    }

    private <T> Set<T> findDuplicates(Collection<T> collection) {

        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for (T t : collection) {
            if (!uniques.add(t)) {
                duplicates.add(t);
            }
        }

        return duplicates;
    }
}
