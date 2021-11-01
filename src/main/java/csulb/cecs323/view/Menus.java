package csulb.cecs323.view;

public class Menus {

    public static void mainMenu(){
        System.out.println("\nMAIN MENU");
        System.out.println("  1) Add New Object");
        System.out.println("  2) List All Object information");
        System.out.println("  3) Delete Book");
        System.out.println("  4) Update a Book");
        System.out.println("  5) List All Primary Keys");
        System.out.println("  6) Exit Program");
    }

    public static void createNewItemMainMenu() {
        System.out.println("\nWhat kind of object would you like to insert?");
        System.out.println("  1) Authoring Entity");
        System.out.println("  2) Publisher");
        System.out.println("  3) Book");
        System.out.println("  4) Go back");
    }

    public static void authoringEntityTypeMenu() {
        System.out.println("\nWhat type of authoring entity?");
        System.out.println("  1) Individual Author");
        System.out.println("  2) Writing Group");
        System.out.println("  3) Ad Hoc Team");
        System.out.println("  4) Add Individual Author to Ad Hoc Group");
        System.out.println("  5) Go Back");
    }

    public static void bookSearchOptions() {
        System.out.println("\nHow would you like to query the book");
        System.out.println("  1) ISBN");
        System.out.println("  2) Title & Authoring Entity");
        System.out.println("  3) Title & Publisher");
        System.out.println("  4) Go Back");
    }
}
