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
}
