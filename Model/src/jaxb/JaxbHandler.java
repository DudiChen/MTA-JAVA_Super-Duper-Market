package jaxb;

import exception.XMLException;
import jaxb.generated.*;

import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JaxbHandler {
    private final static String JAXB_XML_CLASSES_PACKAGE_NAME = "jaxb.generated";
    private final static String INVALID_XML_JAXB_LOAD_MESSAGE = "Invalid XML Format";
    private final static String INVALID_XML_DUPLICATED_ITEM_ID_MESSAGE = "Duplicated Unique ID for Items";
    private final static String INVALID_XML_DUPLICATED_STORE_ID_MESSAGE = "Duplicated Unique ID for Store";
    private final static String XML_FILE_NOT_FOUND_MESSAGE = "XML file does not exist in path";
    private final static String XML_FILE_INVALID_TYPE_MESSAGE = "XML file of invalid type ";

    private boolean isXMLContentValid = false;

    public SuperDuperMarketDescriptor extractXMLData(String xmlPath) throws XMLException, FileNotFoundException, XMLParseException {
        File xmlFile = new File(xmlPath);
        if (!xmlFile.exists()) {
            throw new FileNotFoundException(XML_FILE_NOT_FOUND_MESSAGE);
        }
        else if (!isFileXMLType(xmlPath)) {
            throw new XMLParseException(XML_FILE_INVALID_TYPE_MESSAGE);
        }

        InputStream inputStreamFromXml = JaxbHandler.class.getResourceAsStream(xmlPath);
        SuperDuperMarketDescriptor sdMarketDescriptor = null;
        try {
            sdMarketDescriptor = desrializeFrom(inputStreamFromXml);
        }
        catch (JAXBException ex) {
            throw new XMLException(INVALID_XML_JAXB_LOAD_MESSAGE);
        }
        // TODO: Consider moving here the xml data validation instead of post data mount to memory
//        validateXMLValues(sdMarketDescriptor);

        return sdMarketDescriptor;
    }

    private boolean isFileXMLType(String xmlPath) {
        return (xmlPath.contains(".") && xmlPath.substring(xmlPath.lastIndexOf("." + 1)).equals("xml"));
    }

    private SuperDuperMarketDescriptor desrializeFrom(InputStream inStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_CLASSES_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (SuperDuperMarketDescriptor) unmarshaller.unmarshal(inStream);
    }

    // TODO: Consider requirements for more validation methods; e.g. Same product name with different ids
    private void validateXMLValues(SuperDuperMarketDescriptor sdMarketDescriptor) throws XMLException {
        validateXMLItems(sdMarketDescriptor.getSDMItems());
        validateXMLStores(sdMarketDescriptor.getSDMStores());
    }

    private void validateXMLItems(SDMItems sdmItems) throws XMLException {
        Map<Integer, SDMItem> idToSDMItemMap = new HashMap<>();
        for (SDMItem item : sdmItems.getSDMItem()) {
            int itemID = item.getId();
            if (idToSDMItemMap.get(itemID) == null) {
                idToSDMItemMap.put(itemID, item);
            }
            else {
                throw new XMLException(INVALID_XML_DUPLICATED_ITEM_ID_MESSAGE);
            }
        }
    }

    private void validateXMLStores(SDMStores sdmStores) throws XMLException {
        Map<Integer, SDMStore> idToSDMItemMap = new HashMap<>();
        for (SDMStore store : sdmStores.getSDMStore()) {
            int itemID = store.getId();
            if (idToSDMItemMap.get(itemID) == null) {
                idToSDMItemMap.put(itemID, store);
            } else {
                throw new XMLException(INVALID_XML_DUPLICATED_STORE_ID_MESSAGE);
            }
        }
    }
}

