package csulb.cecs323.view;

import csulb.cecs323.app.*;
import csulb.cecs323.model.*;

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

    /**
     * The view window that initiates the user with the
     * selection on what type of entity to create.
     */
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

    /**
     * The view window that allows the user to create a
     * new authoring entity to the database.
     */
    private void createNewAuthoringEntity_UI() {
        // User chooses what type of authoring entity to create
        Menus.authoringEntityTypeMenu();
        int option = UserInput.getIntRange(1, 5, "Menu Option: ");
        // if user chooses to assign an individual author to an ad hoc group
        if (option == 4) {
            assignAuthorToAdHoc();
        // if the user chooses to create an individual, writing group, or ad hoc group
        } else if (option < 4) {
            boolean newAuthorLoop = true;
            while (newAuthorLoop) {
                // gets the authors new information
                System.out.println("\nenter the following information");
                System.out.print("Authoring Name: ");
                String name = UserInput.getString().trim();
                System.out.print("Email Address: ");
                String email = UserInput.getString().trim();
                // assigns the subclass type to the authoring entity
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
                // validates the new entity and inserts it into the database
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

    /**
     * The view that allows the user to create a new
     * publisher entity to insert into the database.
     */
    private void createNewPublisher_UI() {
        boolean newPublisherLoop = true;
        while (newPublisherLoop) {
            // collect the new publisher information
            System.out.println("\nEnter the following information");
            System.out.print("Publisher Name: ");
            String name = UserInput.getString().trim();
            System.out.print("Email Address: ");
            String email = UserInput.getString().trim();
            System.out.print("Phone Number: ");
            String phone = UserInput.getString().trim();
            // creates the new publisher entity
            Publisher publisher = new Publisher(name, email, phone);
            // validates and inserts the entity into the database
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

    /**
     * The view that allows the user to create a new
     * book entity to be inserted into the database.
     */
    private void createNewBook_UI() {
        boolean newBookLoop = true;
        while (newBookLoop) {
            // gets the new books information
            System.out.println("\nEnter the following information");
            System.out.print("Title: ");
            String title = UserInput.getString().trim();
            System.out.print("ISBN: ");
            String isbn = UserInput.getString().trim();
            Publisher publisher = getValidPublisherFromUser();
            AuthoringEntity ae = getValidAuthorFromUser();
            int year = getValidYearFromUser("Publication Year: ");
            // creates the new book entity
            Book book = new Book(isbn, title, year, publisher, ae);
            // validates and inserts the book into the database
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

    /**
     * Method used to assign an individual author to
     * an ad hoc team.
     */
    private void assignAuthorToAdHoc() {
        boolean assignAuthorLoop = true;
        while (assignAuthorLoop) {
            // gets validated individual author and ad hoc team from user
            System.out.println("\nEnter the following information");
            AdHocTeam team = getValidAdHocTeamFromUser();
            IndividualAuthor author = getValidIndividualAuthorFromUser();
            // if the assignment has not already been made
            if (!team.containsAuthor(author) && !author.containsAdHocTeam(team)) {
                // assigns the author to the ad hoc team and commits it to the db
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

    /**
     * Gets a publishers name from the user and validates that it
     * is an existing publisher within the database. If the publisher
     * does not exist, then the user is prompted to enter another name.
     *
     * @return The selected publisher from the database.
     */
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

    /**
     * Gets an individual Author entity from the database selected
     * by the user based on the given email address entered in. If
     * the given email address is not a valid email address, the user
     * is prompted to reenter another email address.
     *
     * @return The valid individual author from the database.
     */
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

    /**
     * Returns a valid ad hoc team from the database based on the
     * given email address by the user. The user is prompted to re-
     * enter an email if the given email is not a valid ad hoc team
     * email.
     *
     * @return The valid AdHocTeam from the database.
     */
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


    /**
     * Returns an existing author from the database based on the given
     * email address given by the user. If the email address does not
     * query an existing Author, then the user is prompted to enter another
     * email address.
     *
     * @return The valid author from the database.
     */
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

    /**
     * gets an entered year from the user ranging from 1440 to
     * the current year. The earliest printed publication is estimated
     * to be 1440.
     *
     * @param prompt The message displayed to the user prompting their input.
     * @return The year value ranging from 1440 to current year.
     */
    private int getValidYearFromUser(String prompt) {
        int PRINTING_PRESS_INVENTED = 1440;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return UserInput.getIntRange(PRINTING_PRESS_INVENTED, currentYear, prompt);
    }

    /**
     * UI to list Information about different queries
     */
    private void listObjectInformation_UI() {
        System.out.println("\nQueries \n  1) Publisher \n  2) Books \n  3) Writing Group \n");
        int option = UserInput.getIntRange(1, 3, "Option: ");
        int availableInfo = 1;
        switch(option){
            case 1:
                System.out.println("Search By \n  1) Email \n  2) Name \n  3) Phone");
                availableInfo = UserInput.getIntRange(1,3,"Option: ");
                publisherInfo(availableInfo);
                break;
            case 2:
                System.out.println("Search By \n  1) ISBN  \n  2) title and Publisher \n  3) title and Authoring Entity \n");
                availableInfo = UserInput.getIntRange(1,3,"Option: ");
                bookInfo(availableInfo);
                break;
            case 3:
                writingGroupInfo(availableInfo);
                break;

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
                p = getValidPublisherEmail();
                printPublisherInfo(p);
                break;
            case 2:
                p = getValidPublisherFromUser();
                printPublisherInfo(p);
                break;


            case 3:
                p = getValidPublisherPhone();
                printPublisherInfo(p);
                break;
        }

    }

    /**
     * Prints supplied Published Information
     * @param p
     */
    private void printPublisherInfo(Publisher p) {
//        System.out.printf("\nName: %-20s \nEmail: %-20s \nPhone: %-20s\n", p.getName(), p.getEmail(),p.getPhone());
        if (p != null) {
            System.out.printf("\n%-7s %s", "Name: ", p.getName());
            System.out.printf("\n%-7s %s", "Email: ", p.getEmail());
            System.out.printf("\n%-7s %s\n", "Phone: ", p.getPhone());
        }
    }

    /**
     * Gets information based on given coloumns
     * @param availableInfo
     */
    private void bookInfo(int availableInfo) {
        Book recievedBook;
        switch (availableInfo) {

            case 1:
                recievedBook = getBookByIsbnFromUser();
                printBookInfo(recievedBook);
                break;

            case 2:
                recievedBook = getBookByTitleAuthorFromUser();
                printBookInfo(recievedBook);
                break;

            case 3:
                recievedBook = getBookByTitlePublisherFromUser();
                printBookInfo(recievedBook);
                break;
        }

    }

    /**
     * prints information about supplied docs
     * @param book
     */
    private void printBookInfo(Book book){
//        System.out.printf("\nTitle: %-20s \nISBN: %-20s \nYearPublished: %-20s \nAuthorEmail: %-20s \nPublisher: %-20s\n", book.getTitle(), book.getISBN(), book.getYearPublished(), book.getAuthoringEntity().getEmail(), book.getPublisher().getName());
        if(book != null){
        System.out.printf("\n%-16s %s", "Title: ", book.getTitle());
        System.out.printf("\n%-16s %s", "ISBN: ", book.getISBN());
        System.out.printf("\n%-16s %s", "YearPublished: ", book.getYearPublished());
        System.out.printf("\n%-16s %s", "AuthoringEmail: ", book.getAuthoringEntity().getEmail());
        System.out.printf("\n%-16s %s\n", "Publisher: ", book.getPublisher().getName());}
    }

    /**
     * gets WritingGroupInfo
     * @param availableInfo
     */
    private void writingGroupInfo(int availableInfo){
        switch (availableInfo){
            case 1:
                AuthoringEntity ae = getValidWritingGroupFromUser();
//                System.out.printf("\nType: %-20s \nEmail: %-20s \nPhone: %-20s\n", ae.getDiscrimatorValue(),ae.getEmail(), ae.getName());
                if(ae != null){
                System.out.printf("\n%-7s %s", "Type: ", ae.getDiscrimatorValue());
                System.out.printf("\n%-7s %s", "Email: ", ae.getEmail());
                System.out.printf("\n%-7s %s\n", "Phone: ", ae.getName());
        }
        }
    }

    /**
     * gets a valid User from input checks with the db
     * @return Valid Authoring Entity
     */
    private AuthoringEntity getValidWritingGroupFromUser() {
        AuthoringEntity ae = null;
        boolean writingGroupInSystem = false;
        while (!writingGroupInSystem) {
            System.out.print("Authoring Entity Email: ");
            String authorEmail = UserInput.getString().trim();
            ae = bc.findWritingGroupEntity(authorEmail);
            if (ae == null) {
                System.out.println("That is not a recognized Writing Group email.");
            } else {
                writingGroupInSystem = true;
            }
        }
        return ae;
    }

    /**
     * The view that allows the user to delete an existing
     * book from the database.
     */
    private void deleteBook_UI() {
        // allows the user to select a book by ISBN, title & author, or title & publisher
        Menus.bookSearchOptions();
        int option = UserInput.getIntRange(1, 4, "User Option: ");
        if (option != 4) {
            // retrieves the book from the database
            Book book;
            if (option == 1) {
                book = getBookByIsbnFromUser();
            } else if (option == 2) {
                book = getBookByTitleAuthorFromUser();
            } else {
                book = getBookByTitlePublisherFromUser();
            }
            // if the selected book exists in the db
            if (book != null) {
                // print out the selected book and have the user confirm its deletion
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

    /**
     * The view that allows the user to update an existing books
     * author to an existing author in the database.
     */
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

    /**
     * checks entered ISBN with db
     * @returns Valid Book
     */
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

    /**
     * checks book based on title and author in the db
     * @return Valid book
     */
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

    /**
     * Checks book in the db based on title/publisher
     * @return Valid Book
     */
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

    /**
     * Prompts up UI for Primary Key
     */
    private void listPrimaryKeys_UI() {
        System.out.println("1)Books \n 2) Publishers \n 3) Authoring entity\n 4) Exit Interface \n");
        int option = UserInput.getIntRange(1,4,"Option: ");
        switch (option){
            case 1:
                bc.printBookPK();
                break;
            case 2:
                bc.printpublisherPK();
                break;
            case 3:
                bc.printAuthoringEntityPK();
                break;
            case 4: break;
        }
    }

    /**
     * Get's Publisher Based on Email
     * @return Publuisher
     */
    private Publisher getValidPublisherEmail(){
        Publisher publisher = null;
        boolean publisherInSystem = false;
        while (!publisherInSystem) {
            System.out.print("Publisher email: ");
            String pEmail = UserInput.getString().trim();
            publisher = bc.findPublisherUsingEmail(pEmail);
            if (publisher == null) {
                System.out.println("That is not an existing publisher in our database.");
            } else {
                publisherInSystem = true;
            }
        }
        return publisher;
    }

    /**
     * Get Publisher based on Phone
     * @return Publihser
     */
    private Publisher getValidPublisherPhone(){
        Publisher publisher = null;
        boolean publisherInSystem = false;
        while (!publisherInSystem) {
            System.out.print("Publisher Phone: ");
            String pPhone = UserInput.getString().trim();
            publisher = bc.findPublisherUsingPhone(pPhone);
            if (publisher == null) {
                System.out.println("That is not an existing publisher in our database.");
            } else {
                publisherInSystem = true;
            }
        }
        return publisher;
    }
}
