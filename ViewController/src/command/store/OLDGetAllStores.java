//package command.store;
//
////import com.sdp.controller.Main;
//import command.Command;
//import command.Executor;
//import com.sdp.model.ID;
//import com.sdp.model.Store;
//import com.sdp.service.Services;
//import com.sdp.util.Either;
//import com.sdp.util.ErrorMessage;
//import javax.xml.bind.JAXBException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class OLDGetAllStores implements Command {
//    @Override
//    public void execute(Executor controller) {
//        Map<ID, Store> result = null;
//        try {
//            InputStream inputStream = Main.class.getResourceAsStream("/com/sdp/resource/ex1-big.xml");
//            if(inputStream == null) {
//                List<ErrorMessage> fileNotFoundErrors = new ArrayList<ErrorMessage>();
//                fileNotFoundErrors.add(new ErrorMessage("a")); // better to move message string declaration as final to a different place
//                result = Either.left(fileNotFoundErrors);
//            }
//            else {
//                result = new Services().getAll(inputStream, Store.class);
//            }
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//        result.apply(
//                errors -> controller.getView().updateErrors(errors),
//                stores -> controller.getModel().setStores(stores)
//        );
//    }
//}
