package view.menu;

public abstract class MenuItem {
    protected final String title;
    protected MenuItem baseMenuItem;

    protected MenuItem(String titleString)
    {
        title = titleString;
    }

    public void setParent(MenuItem parentMenuItem)
    {
        this.baseMenuItem = parentMenuItem;
    }

    public String getTitle() {
        return title;
    }

    public abstract boolean startMenuItem();


}
