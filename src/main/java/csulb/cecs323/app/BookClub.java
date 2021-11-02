/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;
import csulb.cecs323.view.View;
import csulb.cecs323.errorhandeling.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 */
public class BookClub {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the BookClub
    * class, and create an instance of BookClub in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(BookClub.class.getName());
   /**
    * The constructor for the BookClub class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */

   public BookClub(EntityManager manager) {
      this.entityManager = manager;
   }


   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      LOGGER.setLevel(Level.OFF);
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("BookClub");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of BookClub and store our new EntityManager as an instance variable.
      BookClub bookclub = new BookClub(manager);

      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      View mainView = new View(bookclub);
      mainView.begin();

   } // End of the main method

   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the BookClub entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

   /**
    * Queries a selected book based off of the given
    * ISBN. Returns null if no book is found.
    *
    * @param isbn The ISBN to query with
    * @return The queried book
    */
   public Book selectBookByIsbn(String isbn) {
      try {
         return this.entityManager.find(Book.class, isbn);
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return null;
      }
   }

   /**
    * Selects a given book based off of the given title
    * and publisher. Returns null if no book is found.
    *
    * @param title The title of the book
    * @param publisher The books author
    * @return The queried book
    */
   public Book selectBookTitlePublisher(String title, String publisher) {
      List<Book> bList = this.entityManager.createNamedQuery("FindPublisherUsingTitlePublisherName",
              Book.class).setParameter(1, title).setParameter(2, publisher).getResultList();
      if (bList.size() != 0)
         return bList.get(0);
      else
         return null;
   } // End of selectBookTitlePublisher member method

   /**
    * Selects a book based off of the given title and author.
    * Returns null if no book is found;
    *
    * @param title The title of the book
    * @param author The books author
    * @return The queried book
    */
   public Book selectBookTitleAuthor(String title, String author) {
      List<Book> bList = this.entityManager.createNamedQuery("FindPublisherUsingTitleAuthoringEntityName",
              Book.class).setParameter(1, title).setParameter(2, author).getResultList();
      if (bList.size() != 0)
         return bList.get(0);
      else
         return null;
   } // End of selectBookTitleAuthor member method

   public IndividualAuthor selectIndividualAuthor(String email) {
      return this.entityManager.find(IndividualAuthor.class, email);
   }

   public AdHocTeam selectAdHocTeam(String email) {
      return this.entityManager.find(AdHocTeam.class, email);
   }

   public void validateNewBook(Book newBook) throws PrimaryKeyConstraintException, UniqueConstraintException {
      Book queriedBook = selectBookByIsbn(newBook.getISBN());
      if (queriedBook != null)
         throw new PrimaryKeyConstraintException(newBook.getISBN() + " is already an existing ISBN");

      queriedBook = selectBookTitlePublisher(newBook.getTitle(), newBook.getPublisher().getName());
      if (queriedBook != null)
         throw new UniqueConstraintException("Invalid title and publisher exception.");

      queriedBook = selectBookTitleAuthor(newBook.getTitle(), newBook.getAuthoringEntity().getEmail());
      if (queriedBook != null)
         throw new UniqueConstraintException("Invalid title and author exception.");
   }

   public void validateNewPublisher(Publisher newPublisher) throws PrimaryKeyConstraintException, UniqueConstraintException {
      Publisher queriedPublisher = findPublisherUsingName(newPublisher.getName());
      if (queriedPublisher != null)
         throw new PrimaryKeyConstraintException("The publisher name \"" + newPublisher.getName() + "\" already exists.");

      queriedPublisher = findPublisherUsingEmail(newPublisher.getEmail());
      if (queriedPublisher != null)
         throw new UniqueConstraintException("Email address \"" + newPublisher.getEmail() + "\" already exists.");

      queriedPublisher = findPublisherUsingPhone(newPublisher.getPhone());
      if (queriedPublisher != null)
         throw new UniqueConstraintException("That Phone number already exists in our system.");
   }

   public void validateNewAuthor(AuthoringEntity ae) throws PrimaryKeyConstraintException {
      AuthoringEntity queriedAuthor = findAuthoringEntity(ae.getEmail());
      if (queriedAuthor != null)
         throw new PrimaryKeyConstraintException("Email address \"" + ae.getEmail() + "\" already exists.");
   }

   public <E> void insertItems(List<E> list) {
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      createEntity(list);
      tx.commit();
   }

   // TODO - remove function and replace usage to persistClass()
   public <E> void insertSingleItem(E item) {
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      entityManager.persist(item);
      tx.commit();
   }

   public <E> void persistClass(E item) {
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      entityManager.persist(item);
      tx.commit();
   }

   public void deleteBook(Book book) {
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      entityManager.remove(book);
      tx.commit();
   }

   /**
    * Returns AuthoringEntity using PK
    * @param email
    * @return searched AuthoringEntity
    */
   public AuthoringEntity findAuthoringEntity(String email){
      try {
         return this.entityManager.find(AuthoringEntity.class, email);
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return null;
      }
   }

   /**
    *
    * @return
    */
   public Publisher findPublisherUsingName(String name){
      try {
         return this.entityManager.find(Publisher.class, name);
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return null;
      }
   }

   public Publisher findPublisherUsingEmail(String email){
      try {
         return entityManager.createNamedQuery("FindPublisherUsingEmail", Publisher.class).setParameter(1, email).getSingleResult();
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
//         System.out.println(e.getMessage());
         return null;
      }
   }


   public Publisher findPublisherUsingPhone(String phone){
      try {
         return entityManager.createNamedQuery("FindPublisherUsingPhone", Publisher.class).setParameter(1, phone).getSingleResult();
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
//         System.out.println(e.getMessage());
         return null;
      }
   }

   /**
    * prints Email and name of all AuthoringEntities in the db
    */
   public void printAuthoringEntityPK(){
      List<AuthoringEntity> ae = entityManager.createNamedQuery("AuthoringEntitiesPK", AuthoringEntity.class).getResultList();
      if(ae.size() == 0){
         System.out.println("Authoring Entity table is empty");
      }
      for(AuthoringEntity authEnt: ae){
         System.out.println("Email: "+ authEnt.getEmail()  + " |Type: " + authEnt.getDiscrimatorValue());
      }
   }

   /** prints name of all publishers in the DB
    *
    */
   public void printpublisherPK(){
      List<Publisher> publishers = entityManager.createNamedQuery("PublisherPK").getResultList();
      if(publishers.size() == 0){
         System.out.println("Publisher table is empty");
      }
      for(Publisher pub: publishers){
         System.out.println("name: " + pub.getName());
      }
   }

   /** prints ISBN and title of all books in the DB
    *
    */
   public void printBookPK(){
      List<Book> books = entityManager.createNamedQuery("BookPK").getResultList();
      if(books.size() == 0){
         System.out.println("Table Books is Empty");
      }
      for(Book bk: books){
         System.out.println("ISBN: " + bk.getISBN() + " Title: " + bk.getTitle());
      }
   }
} // End of BookClub class
