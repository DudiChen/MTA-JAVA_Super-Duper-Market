package adapters;

//import com.sdp.model.AppData;
import entities.AppData;
import entities.Product;
import entities.Store;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMStore;
import jaxb.generated.SuperDuperMarketDescriptor;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppDataAdapter extends XmlAdapter<SuperDuperMarketDescriptor, AppData> {

    private Map<Integer, Product> idToProduct;
    private Map<Integer, Store> idToStore;

    @XmlJavaTypeAdapter(IdToStoreAdapter.class)
    public Map<Integer, Store> getIdToStore() {
        return idToStore;
    }

    @XmlJavaTypeAdapter(IdToProductAdapter.class)
    public Map<Integer, Product> getIdToProduct() {
        return idToProduct;
    }

    @Override
    public AppData unmarshal(SuperDuperMarketDescriptor sdm) throws ValidationException {
//        StringBuilder errMsg = new StringBuilder();
//        checkForNonExistingProductsInStores(sdm.getSDMItems().getSDMItem(),sdm.getSDMStores().getSDMStore())
//                .ifPresent(err -> errMsg.append(err));
//        if(errMsg.toString().isEmpty()){
            return new AppData(idToProduct, idToStore);
//        }
//        else {
//            throw new ValidationException(errMsg.toString());
//        }
    }

    @Override
    public SuperDuperMarketDescriptor marshal(AppData v) {
        return null;
    }

    private Optional<String> checkForNonExistingProductsInStores(List<SDMItem> products, List<SDMStore> stores) {
//        String err = String.join(System.lineSeparator(),
//                products.stream()
//                        .filter(product -> )
//                        .map(nonExistingProduct -> "product " + nonExistingProduct.getName() + " doesn't exist")
//                        .collect(Collectors.toList()));
//        if(err.isEmpty()) {
//            return Optional.empty();
//        }
//        else {
//            return Optional.of(err);
//        }
        return Optional.empty();
    }
}
