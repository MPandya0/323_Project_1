package csulb.cecs323.view;

import csulb.cecs323.app.*;

import csulb.cecs323.model.*;

import csulb.cecs323.model.AuthoringEntity;
import csulb.cecs323.model.Book;
import csulb.cecs323.model.Publisher;

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
        int option = UserInput.getIntRange(1, 5, "Menu Option: ");
        if (option == 4) {
            assignAuthorToAdHoc();
        } else if (option < 4) {
            boolean newAuthorLoop = true;
            while (newAuthorLoop) {

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

                try {
                    bc.validateNewAuthor(ae);
                    bc.insertSingleItem(ae);
                    System.out.println("\n" + ae.getName() + " inserted into database.");
                    newAuthorLoop = false;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    newAuthorLoop = UserInput.getYesNo("Re-enter Information [y/n]: ");
                }
            }
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

            Publisher publisher = new Publisher(name, email, phone);

            try {
                bc.validateNewPublisher(publisher);
                bc.insertSingleItem(publisher);
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

            Book book = new Book(isbn, title, year, publisher, ae);

            try {
                bc.validateNewBook(book);
                bc.insertSingleItem(book);
                System.out.println("\n" + book.getTitle() + " inserted into database.");
                newBookLoop = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                newBookLoop = UserInput.getYesNo("Re-enter Information [y/n]: ");
            }
        }
    }

    private void assignAuthorToAdHoc() {
        boolean assignAuthorLoop = true;
        while (assignAuthorLoop) {
            System.out.println("\nEnter the following information");
            AdHocTeam team = getValidAdHocTeamFromUser();
            IndividualAuthor author = getValidIndividualAuthorFromUser();

            if (!team.containsAuthor(author) && !author.containsAdHocTeam(team)) {
                team.insertAuthor(author);
                author.insertAdHocTeam(team);
                bc.persistClass(team);
                bc.persistClass(author);
                System.out.println("\n" + author.getName() + " assigned to " + team.getName());
            } else {
                System.out.println("\nThat Assignment has already been made");
            }
            assignAuthorLoop = UserInput.getYesNo("Assign Another Author [y/n]: ");
        }
    }

    private Publisher getValidPublisherFromUser() {
        Publisher publisher = null;
        boolean publisherInSystem = false;
        while (!publisherInSystem) {
            System.out.print("Publisher name: ");
            String pName = UserInput.getString().trim();
            publisher = bc.findPublisherUsingName(pName);
            if (publisher == null) {
                System.out.println("That is not an existing publisher in our database.");
            } else {
                publisherInSystem = true;
            }
        }
        return publisher;
    }

    private IndividualAuthor getValidIndividualAuthorFromUser() {
        IndividualAuthor ia = null;
        boolean authorInSystem = false;
        while (!authorInSystem) {
            System.out.print("Authors Email: ");
            String email = UserInput.getString().trim();
            ia = bc.selectIndividualAuthor(email);
            if (ia == null) {
                System.out.println("That is not a recognized individual authors email");
            } else {
                authorInSystem = true;
            }
        }
        return ia;
    }

    private AdHocTeam getValidAdHocTeamFromUser() {
        AdHocTeam team = null;
        boolean teamInSystem = false;
        while (!teamInSystem) {
            System.out.print("Ad Hoc Team Email: ");
            String email = UserInput.getString().trim();
            team = bc.selectAdHocTeam(email);
            if (team == null) {
                System.out.println("That is not a recognized ad hoc team email");
            } else {
                teamInSystem = true;
            }
        }
        return team;
    }

    private AuthoringEntity getValidAuthorFromUser() {
        AuthoringEntity ae = null;
        boolean authorInSystem = false;
        while (!authorInSystem) {
            System.out.print("Authoring Entity Email: ");
            String authorEmail = UserInput.getString().trim();
            ae = bc.findAuthoringEntity(authorEmail);
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
        System.out.println("Queries \n 1) Publisher \n 2) Books \n 3) Writing Group \n");
        int option = UserInput.getIntRange(1, 3, "Option: ");
        int availableInfo = 1;
        switch(option){
            case 1:
                System.out.println("Search By \n 1) Email \n 2) Name \n 3) Phone");
                availableInfo = UserInput.getIntRange(1,3,"Option: ");
                publisherInfo(availableInfo);
            case 2:
                System.out.println("Search By \n 1)ISBN  \n 2) title and Publisher \n 3) title and Authoring Entity \n");
                availableInfo = UserInput.getIntRange(1,3,"Option: ");
                bookInfo(availableInfo);
            case 3:
                writingGroupInfo(availableInfo);

        }

    }

    /**Info functions retrieve information from tables
     * based on what information you can give them
     * @param availableInfo
     * @return queriedClass
     */
    private void publisherInfo(int availableInfo){
        Publisher p;
        switch(availableInfo){
            case 1:
                System.out.println("Email: ");
                String email = UserInput.getString();
                p = bc.findPublisherUsingEmail(email);
                System.out.printf("%20s \n %20s \n %20s", p.getName(), p.getEmail(), p.getPhone());

            case 2:
                System.out.println("Name: ");
                String name = UserInput.getString();
                p = bc.findPublisherUsingName(name);
                System.out.printf("%20s \n %20s \n %20s", p.getName(), p.getEmail(), p.getPhone());

                case 3:
                System.out.println("Phone: ");
                String phone = UserInput.getString();
                p = bc.findPublisherUsingPhone(phone);
                System.out.printf("%20s \n %20s \n %20s", p.getName(), p.getEmail(), p.getPhone());

        }

    }

    private void bookInfo(int availableInfo) {
        Book recievedBook;
        switch (availableInfo) {

            case 1:
                System.out.print("Enter ISBN: ");
                String ISBN = UserInput.getString();
                recievedBook=  bc.selectBookByIsbn(ISBN);
                System.out.printf("%-15s \n %-40s \n %-40s", recievedBook, recievedBook.getTitle(), recievedBook.getYearPublished() );

            case 2:
                System.out.println("title");
                String title = UserInput.getString();

                System.out.println("Publisher");
                String publisher = UserInput.getString();

                recievedBook = bc.selectBookTitlePublisher(title, publisher);
                System.out.printf("%-15s \n %-40s \n %-40s", recievedBook.getISBN(), recievedBook.getTitle(), recievedBook.getYearPublished() );

            case 3:
                System.out.println("title");
                String title1 = UserInput.getString();

                System.out.println("Authoring Entity");
                String author = UserInput.getString();

                recievedBook = bc.selectBookTitleAuthor(title1, author);
                System.out.printf("%-15s \n %-40s \n %-40s", recievedBook, recievedBook.getTitle(), recievedBook.getYearPublished() );
        }

    }

    private void writingGroupInfo(int availableInfo){
        switch (availableInfo){
            case 1:
                System.out.println("Email: \n");
                String email = UserInput.getString();

                AuthoringEntity ae = bc.findAuthoringEntity(email);
                System.out.printf("%15s \n %15s", ae.getName(), ae.getEmail());
        }

    }



    private void deleteBook_UI() {
        Menus.bookSearchOptions();
        int option = UserInput.getIntRange(1, 4, "User Option: ");
        if (option != 4) {
            Book book;
            if (option == 1) {
                book = getBookByIsbnFromUser();
            } else if (option == 2) {
                book = getBookByTitleAuthorFromUser();
            } else {
                book = getBookByTitlePublisherFromUser();
            }
            if (book != null) {
                System.out.println("\nSelected Book: " + book + "\n");
                boolean confirmDelete = UserInput.getYesNo("Are you sure you wish to delete " +
                        book.getTitle() + " from file [y/n]: ");
                if (confirmDelete) {
                    System.out.println(book.getTitle() + " deleted");
                    bc.deleteBook(book);
                } else {
                    System.out.println(book.getTitle() + " not deleted.");
                }
            }
        }
    }

    private void updateBook_UI() {
        Menus.bookSearchOptions();
        int option = UserInput.getIntRange(1, 4, "User Option: ");
        if (option != 4) {
            Book book;
            if (option == 1) {
                book = getBookByIsbnFromUser();
            } else if (option == 2) {
                book = getBookByTitleAuthorFromUser();
            } else {
                book = getBookByTitlePublisherFromUser();
            }
            if (book != null) {
                System.out.println("\nSelected Book: " + book);
                System.out.println("Current authoring entity: " + book.getAuthoringEntity().getName());
                System.out.println("\nEnter a new Authoring Entity");
                AuthoringEntity ae = getValidAuthorFromUser();
                book.setAuthoringEntity(ae);
                bc.persistClass(book);
                System.out.println(ae.getName() + " now assigned to " + book.getTitle());
            }
        }
    }

    private Book getBookByIsbnFromUser() {
        Book book = null;
        boolean collectInfoLoop = true;
        while (collectInfoLoop) {
            System.out.println("Enter the following information");
            System.out.print("ISBN: ");
            String isbn = UserInput.getString().trim();
            book = bc.selectBookByIsbn(isbn);
            if (book != null) {
                collectInfoLoop = false;
            } else {
                collectInfoLoop = UserInput.getYesNo("\nThat ISBN does not exist.\nEnter another ISBN [y/n]: ");
            }
        }
        return book;
    }

    private Book getBookByTitleAuthorFromUser() {
        Book book = null;
        boolean collectInfoLoop = true;
        while (collectInfoLoop) {
            System.out.println("Enter the following information");
            System.out.print("Title: ");
            String title = UserInput.getString().trim();
            System.out.print("Author's Email: ");
            String email = UserInput.getString().trim();
            book = bc.selectBookTitleAuthor(title, email);
            if (book != null) {
                collectInfoLoop = false;
            } else {
                System.out.println("\nThe provided information does not match any book on file.");
                collectInfoLoop = UserInput.getYesNo("Enter another title and author? [y/n]: ");
            }
        }
        return book;
    }

    private Book getBookByTitlePublisherFromUser() {
        Book book = null;
        boolean collectInfoLoop = true;
        while (collectInfoLoop) {
            System.out.println("Enter the following information");
            System.out.print("Title: ");
            String title = UserInput.getString().trim();
            System.out.print("Publisher name: ");
            String pName = UserInput.getString().trim();
            book = bc.selectBookTitlePublisher(title, pName);
            if (book != null) {
                collectInfoLoop = false;
            } else {
                System.out.println("\nThe provided information does not match any book on file.");
                collectInfoLoop = UserInput.getYesNo("Enter another title and publisher? [y/n]: ");
            }
        }
        return book;
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
        bc.insertItems(pList);
        System.out.println("Adding second publisher");
        pList.clear();
        pList.add(p2);

        num = UserInput.getIntRange(0, 100, "Enter a number: ");

        try {
            bc.insertItems(pList);
        } catch (Exception e) {//
            System.out.println(e.getMessage());
        }
    }
}
