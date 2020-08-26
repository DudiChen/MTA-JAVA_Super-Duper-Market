package view.menu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuItemNonActionable extends MenuItem {
    private List<MenuItem> subMenuItems;

    public MenuItemNonActionable(String titleNameString)
    {
        super(titleNameString);
    }

    @Override
    public boolean startMenuItem()
    {
        showSubMenuItemTitles();
        int userChoice = getUserChoice();

        while (userChoice != 0)
        {
            subMenuItems.get(userChoice -1).startMenuItem();
            showSubMenuItemTitles();
            userChoice = getUserChoice();
        }

        return true;
    }

    public void addSubMenuItem(MenuItem menuItem)
    {
        if (subMenuItems == null)
        {
            subMenuItems = new ArrayList<>();
        }

        subMenuItems.add(menuItem);
        menuItem.setParent(this);
    }

    private void showSubMenuItemTitles()
    {
        int choiceCounter = 0;
        StringBuilder menuStringBuilder = new StringBuilder();
        menuStringBuilder.append(":: " + this.title + " ::" + System.lineSeparator());
        if (baseMenuItem == null)
        {
            menuStringBuilder.append("0. Exit" + System.lineSeparator());
        }
        else
        {
            menuStringBuilder.append("0. Back" + System.lineSeparator());
        }

        for (MenuItem subMenuItem : subMenuItems)
        {
            menuStringBuilder.append(++choiceCounter + ". " + subMenuItem.title + System.lineSeparator());
        }

        System.out.println(menuStringBuilder.toString());
    }

    private int getUserChoice()
    {
        System.out.println("Choose from the choices above:");
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);
        boolean isParsed = true;
        do {
            try {
                userInput = scanner.nextInt();
            } catch(InputMismatchException e) {
                promptUserForInvalidChoiceInput();
                isParsed = false;
            }
        }
        while (!isParsed || userInput < 0 || userInput > subMenuItems.size());

        return userInput;
    }

    private void promptUserForInvalidChoiceInput() {
        System.out.println("input not valid try again:");
    }
}
