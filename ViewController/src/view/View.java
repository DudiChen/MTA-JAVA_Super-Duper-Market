//package view;
//
//import com.sdp.controller.command.Executor;
//import com.sdp.controller.command.store.GetAllStores;
//import com.sdp.model.AppData;
//import com.sdp.model.ID;
//import com.sdp.model.Store;
//import com.sdp.util.Either;
//import com.sdp.util.ErrorMessage;
//
//import java.util.List;
//import java.util.Map;
//
//public class View {
//
//    private AppData appData;
//    private Executor executor;
//
//    public View(Executor executor, AppData appData) {
//        this.executor = executor;
//        this.appData = appData;
//    }
//
//    public void run() {
//        executor.executeOperation(new GetAllStores());
////        executor.executeOperation(new AddStore(new Store(...)));
////        executor.executeOperation(AddProduct(new Product(...)));
//    }
//
//    public void onStoresUpdate(Either<List<ErrorMessage>, Map<ID, Store>> hopefullyStores) { // register with this method at the model who implements observer design pattern
//        hopefullyStores.apply(
//                errorsMessages -> System.out.println("errors:" + errorsMessages),
//                idToStore -> System.out.println("fetched all stores" + idToStore)
//        );
//    }
//
//    public void updateErrors(List<ErrorMessage> errors) {
//        System.out.println("errors: " + errors);
//    }
//}
