package view.menu.item;

import entity.Store;
import entity.market.InvoiceProduct;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;

public class InvoiceProductContent extends ListCell<InvoiceProduct> {

    @Override
    protected void updateItem(InvoiceProduct invoiceProduct, boolean empty) {
        super.updateItem(invoiceProduct, empty);
        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
//                System.out.println("    ==== " + invoiceProduct.getName() + " ====");
//                System.out.println("        ID: " + invoiceProduct.getId());
//                System.out.println("        Purchase Method: " + invoiceProduct.getPurchaseMethod());
//                System.out.println("        Price: " + invoiceProduct.getPrice());
//                System.out.println("        Quantity: " + invoiceProduct.getQuantity());
//                System.out.println("        Total Price: " + invoiceProduct.getTotalPrice());
//

        }
    }
}
