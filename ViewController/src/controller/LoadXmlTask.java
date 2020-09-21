package controller;

import builder.MarketBuilder;
import entity.market.Market;
import exception.XMLException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import jaxb.JaxbHandler;

import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.FileNotFoundException;

public class LoadXmlTask extends Task<Market> {
    private final static String XML_FILE_NOT_FOUND_MESSAGE = "XML file does not exist in path";
    private final static String XML_FILE_INVALID_TYPE_MESSAGE = "XML file of invalid type ";
    private String fullFilePath;

    public LoadXmlTask(String fullFilePath) {
        this.fullFilePath = fullFilePath;
        this.updateMessage("Started ...");
        this.updateProgress(0.1, 1);
    }

    @Override
    protected Market call() throws FileNotFoundException, XMLParseException, XMLException, ValidationException, InterruptedException {
        JaxbHandler jaxbHandler = new JaxbHandler();
        Platform.runLater(() -> updateMessage("Opening File ..."));
        Thread.sleep(1000);
        File xmlFile = new File(this.fullFilePath);
        if (!xmlFile.exists()) {
            throw new FileNotFoundException(XML_FILE_NOT_FOUND_MESSAGE);
        } else if (!isFileXMLType(this.fullFilePath)) {
            throw new XMLParseException(XML_FILE_INVALID_TYPE_MESSAGE);
        }
        Platform.runLater(() -> updateProgress(0.3, 1));
        Platform.runLater(() -> updateMessage("File Opened Successfully"));
        Thread.sleep(500);
        Platform.runLater(() -> updateProgress(0.5, 1));
        Thread.sleep(1000);
        Platform.runLater(() -> updateMessage("Building Objects ..."));
        Thread.sleep(1000);
        Market market = null;
        try {
            market =  new MarketBuilder().build(jaxbHandler.extractXMLData(xmlFile));
            Platform.runLater(() -> updateProgress(1, 1));
            Platform.runLater(() -> updateMessage("Finishing ..."));
            Thread.sleep(500);
        } catch (Exception e) {
            Platform.runLater(() -> updateProgress(0, 1));
            Platform.runLater(() -> updateMessage("Failed ..."));
            Thread.sleep(1000);
            Platform.runLater(() -> updateMessage(""));
            throw e;
        }

        return market;
    }

    private boolean isFileXMLType(String xmlPath) {
        return (xmlPath.contains(".") && xmlPath.substring(xmlPath.lastIndexOf(".")).equals(".xml"));
    }
}
