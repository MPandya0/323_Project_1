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
   public EntityManager entityManager; // TODO - change back to private after all functions are written.

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

      Publisher p = bookclub.findPublisherUsingEmail("simon.schuster@yahoo.com");
      System.out.println(p.getName());
      View mainView = new View(bookclub);
      mainView.begin();
      //test out publisher Query

       //      List<Publisher> p = manager.createNamedQuery("FindPublisherUsingName" ,Publisher.class).setParameter(1, "myname").getResultList();
//      System.out.println(p.size());
//
//      //test out wrting group query
//      List<AuthoringEntity> wg = manager.createNamedQuery("FindWritingGroupUsingEmail", AuthoringEntity.class).setParameter(1, "margaret@hotmail.com").getResultList();
//      System.out.println(wg.size());
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

   public <E> void insertItem(List<E> list) {
      EntityTransaction tx = entityManager.getTransaction();
      tx.begin();
      createEntity(list);
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
         List<Publisher> p = entityManager.createNamedQuery("FindPublisherUsingEmail", Publisher.class).setParameter(1, email).getResultList();
         System.out.println(p.size());
         return p.get(0);
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return null;
      }
   }


   public Publisher findPublisherUsingPhone(String phone){
      try {
         Publisher p = entityManager.createNamedQuery("FindPublisherUsingPhone", Publisher.class).setParameter(1, phone).getSingleResult();
         return p;
      } catch (IllegalArgumentException e) {
         System.out.println("Illegal Argument Exception");
         System.out.println(e.getMessage());
         return null;
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return null;
      }
   }

} // End of BookClub class
