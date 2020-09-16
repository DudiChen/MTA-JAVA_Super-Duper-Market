package view;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import view.menu.BackButtonPage;
import view.menu.Navigatable;
import java.util.Stack;

public class ApplicationContext {
    private Tab rootTab;
    private Stack<Navigatable> pagesStack;
    private BackButtonPage backPage;

    public ApplicationContext() {
        this.pagesStack = new Stack<>();
    }

    public void navigate(Navigatable navigatable) {
        if(this.pagesStack.empty()) {
            this.rootTab.setContent(navigatable.getContent());
            this.pagesStack.push(navigatable);
            return;
        }
        if(this.pagesStack.size() == 1) {
            initBackPage();
        }
        setContainerContent(navigatable.getContent());
        this.pagesStack.push(navigatable);
    }

    private void setContainerContent(Node content) {
        AnchorPane contentPart = this.backPage.getMainContent();
        contentPart.getChildren().clear();
        contentPart.getChildren().add(content);
    }

    private void navigateBack() {
        if(this.pagesStack.empty()) {
            this.backPage = null;
            return;
        }
        // remove current page
        this.pagesStack.pop();
        // get back page
        Navigatable backScreen = this.pagesStack.peek();

        if(this.pagesStack.size() == 1) {
            this.backPage.getMainContent().getChildren().clear();
            this.rootTab.setContent(backScreen.getContent());
            return;
        }
        else {
            setContainerContent(backScreen.getContent());
        }
    }

    public void setRoot(Tab root) {
        this.rootTab = root;
        this.pagesStack.removeAllElements();
        this.backPage = null;
    }

    private void initBackPage() {
        this.backPage = new BackButtonPage(this::navigateBack);
        this.rootTab.setContent(this.backPage.getContent());
    }
}
