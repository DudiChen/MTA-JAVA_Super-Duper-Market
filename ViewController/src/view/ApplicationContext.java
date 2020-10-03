package view;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import view.menu.BackButtonMenu;
import view.menu.Navigatable;
import java.util.Stack;

public class ApplicationContext {
    private Tab rootTab;
    private Stack<Navigatable> pagesStack;
    private BackButtonMenu backPage;
    private static ApplicationContext instance;
    private ApplicationContext() {
        this.pagesStack = new Stack<>();
    }

    public static ApplicationContext getInstance() {
        if(instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
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
        VBox contentPart = this.backPage.getMainContent();
        contentPart.getChildren().clear();
        contentPart.getChildren().add(content);
    }

    public void navigateBack() {
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
        this.backPage = new BackButtonMenu(this::navigateBack);
        this.rootTab.setContent(this.backPage.getContent());
    }
}
