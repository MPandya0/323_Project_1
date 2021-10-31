package csulb.cecs323.view;

import csulb.cecs323.app.*;
import csulb.cecs323.model.Publisher;


import java.util.ArrayList;
import java.util.List;


public class View {

    private BookClub bc;

    public View(BookClub bc){
        this.bc = bc;
    }

    public void begin() {
        boolean continueProgram = true;
        while (continueProgram){
            Menus.mainMenu();
            int option = UserInput.getIntRange(1, 6, "Menu Option: ");
            switch (option) {
                case 1:
                    addNewObject_UI();
                    break;
                case 2:
                    listObjectInformation_UI();
                    break;
                case 3:
                    deleteBook_UI();
                    break;
                case 4:
                    updateBook_UI();
                    break;
                case 5:
                    listPrimaryKeys_UI();
                    break;
                case 6:
                    continueProgram = false;
                    break;
            }
        }
        System.out.println("PROGRAM COMPETE");
    }

    private void addNewObject_UI() {
        System.out.println("Add new object function");
    }

    private void listObjectInformation_UI() {
        System.out.println("List object info function");
    }

    private void deleteBook_UI() {
        System.out.println("Delete Book function");
    }

    private void updateBook_UI() {
        System.out.println("Book update function");
    }

    private void listPrimaryKeys_UI() {
        System.out.println("list primary keys function");
    }

    private void testArea() {
        int num = UserInput.getIntRange(0, 100, "Enter a number: ");

        Publisher p1 = new Publisher("test_P", "stuff@stuff.com", "555-555-5555");
        Publisher p2 = new Publisher("test_P", "stuff@stuff.com", "555-555-5555");
        List<Publisher> pList = new ArrayList<>();
        pList.add(p1);

        System.out.println("Adding first publisher");
        bc.insertItem(pList);
        System.out.println("Adding second publisher");
        pList.clear();
        pList.add(p2);

        num = UserInput.getIntRange(0, 100, "Enter a number: ");

        try {
            bc.insertItem(pList);
        } catch (Exception e) {//
            System.out.println(e.getMessage());
        }
    }
}
