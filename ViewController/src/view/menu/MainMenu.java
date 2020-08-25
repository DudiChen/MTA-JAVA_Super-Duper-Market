package view.menu;

import controller.Controller;

public class MainMenu {
    private final MenuItem menuItem;

    public MainMenu(MenuItem mainMenuItem)
    {
        this.menuItem = mainMenuItem;
    }

    public void show()
    {
        boolean closeMenuBoolean = false;
        while(!closeMenuBoolean)
        {
            closeMenuBoolean = menuItem.startMenuItem();
        }
    }
}
