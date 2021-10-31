package csulb.cecs323.view;

import csulb.cecs323.app.*;
import csulb.cecs323.model.*;
import csulb.cecs323.errorhandeling.*;

import java.util.ArrayList;
import java.util.Calendar;
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
        Menus.createNewItemMainMenu();
        int option = UserInput.getIntRange(1, 4, "Menu Option: ");
        if (option == 1) {
            createNewAuthoringEntity_UI();
        } else if (option == 2) {
            createNewPublisher_UI();
        } else if (option == 3) {
            createNewBook_UI();
        }
    }

    private void createNewAuthoringEntity_UI() {
        Menus.authoringEntityTypeMenu();
        int option = UserInput.getIntRange(1, 4, "Menu Option: ");
        if (option != 4) {
            System.out.println("\nenter the following information");
            System.out.print("Authoring Name: ");
            String name = UserInput.getString().trim();
            System.out.print("Email Address: ");
            String email = UserInput.getString().trim();

            AuthoringEntity ae;
            if (option == 1) {
                ae = new IndividualAuthor(email, name);
            } else if (option == 2) {
                System.out.print("Head Writer: ");
                String headWriter = UserInput.getString().trim();
                int year = getValidYearFromUser("Year Formed: ");
                ae = new WritingGroup(headWriter, year, email, name);
            } else {
                ae = new AdHocTeam(email, name);
            }

            List<AuthoringEntity> aeList = new ArrayList<>();
            aeList.add(ae);
            System.out.println(ae.getClass().getName());
        }
    }

    private void createNewPublisher_UI() {
        boolean newPublisherLoop = true;
        while (newPublisherLoop) {
            System.out.println("\nEnter the following information");
            System.out.print("Publisher Name: ");
            String name = UserInput.getString().trim();
            System.out.print("Email Address: ");
            String email = UserInput.getString().trim();
            System.out.print("Phone Number: ");
            String phone = UserInput.getString().trim();

            // TODO - change to single item insert
            Publisher publisher = new Publisher(name, email, phone);
            List<Publisher> publisherList = new ArrayList<>();
            publisherList.add(publisher);

            try {
                // validator
                bc.insertItem(publisherList);
                System.out.println("\n" + publisher.getName() + " inserted into database.");
                newPublisherLoop = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                newPublisherLoop = UserInput.getYesNo("Re-enter Information [y/n]: ");
            }
        }
    }

    private void createNewBook_UI() {
        boolean newBookLoop = true;
        while (newBookLoop) {
            System.out.println("\nEnter the following information");
            System.out.print("Title: ");
            String title = UserInput.getString().trim();
            System.out.print("ISBN: ");
            String isbn = UserInput.getString().trim();
            Publisher publisher = getValidPublisherFromUser();
            AuthoringEntity ae = getValidAuthorFromUser();
            int year = getValidYearFromUser("Publication Year: ");

            // TODO - change to single item insert
            Book book = new Book(isbn, title, year, publisher, ae);
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);

            try {
                bc.validateNewBook(book);
                bc.insertItem(bookList);
                System.out.println("\n" + book.getTitle() + " inserted into database.");
                newBookLoop = false;
            } catch (PrimaryKeyConstraintException e) {
                System.out.println(e.getMessage());
                newBookLoop = UserInput.getYesNo("Re-enter Information [y/n]: ");
            } catch (UniqueConstraintException e) {
                System.out.println(e.getMessage());
                newBookLoop = UserInput.getYesNo("Re-enter Information [y/n]: ");
            }
        }
    }

    private Publisher getValidPublisherFromUser() {
        Publisher publisher = null;
        boolean publisherInSystem = false;
        while (!publisherInSystem) {
            System.out.print("Publisher name: ");
            String pName = UserInput.getString().trim();
            // TODO - replace code with bc function (get publisher)
            publisher = bc.entityManager.find(Publisher.class, pName);
            if (publisher == null) {
                System.out.println("That is not an existing publisher in our database.");
            } else {
                publisherInSystem = true;
            }
        }
        return publisher;
    }

    private AuthoringEntity getValidAuthorFromUser() {
        AuthoringEntity ae = null;
        boolean authorInSystem = false;
        while (!authorInSystem) {
            System.out.print("Authoring Entity Email: ");
            String authorEmail = UserInput.getString().trim();
            // TODO - replace code with bc function (get author)
            ae = bc.entityManager.find(AuthoringEntity.class, authorEmail);
            if (ae == null) {
                System.out.println("That is not a recognized authoring entity email.");
            } else {
                authorInSystem = true;
            }
        }
        return ae;
    }

    private int getValidYearFromUser(String prompt) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return UserInput.getIntRange(0, currentYear, prompt);
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
