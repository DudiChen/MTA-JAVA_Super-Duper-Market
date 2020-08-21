package service;
import jaxb.generated.ObjectFactory;
import util.Either;
import util.ErrorMessage;

import javax.xml.bind.*;
import java.io.InputStream;
import java.util.*;

public class Services {

    private final static String JAXB_GENERATED = "com.sdp.resources.jaxb";

    public static <T> Either<List<ErrorMessage>, T> get(InputStream inputStream) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        T result = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            result = ((JAXBElement<T>) jaxbContext.createUnmarshaller().unmarshal(inputStream)).getValue();
        } catch (ValidationException e) {
            errorMessages.add(new ErrorMessage(e.getMessage()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        if(errorMessages.size() > 0) {
            return Either.left(errorMessages);
        }
        else {
            return Either.right(result);
        }
    }
}
