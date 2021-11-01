package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniquePublisher", columnNames = {"title", "publisher_name"}),
        @UniqueConstraint(name = "UniqueAuthor", columnNames = {"title", "AUTHORING_ENTITY_EMAIL"})
})
@NamedNativeQuery(
        name = "FindPublisherUsingTitlePublisherName",
        query = "Select B.* " +
                "From Book B " +
                "Where B.title = ? and B.publisher_name =  ? ",
        resultClass = Book.class
)
@NamedNativeQuery(
        name = "FindPublisherUsingTitleAuthoringEntityName",
        query = "Select B.* " +
                "From Book B " +
                "Where B.title = ? and B.AUTHORING_ENTITY_EMAIL = ? ",
        resultClass = Book.class
)
public class Book {
    @Id
    @Column(length = 17, nullable = false)
    private String ISBN;

    @Column(length = 80, nullable = false)
    private String title;

    @Column(nullable = false)
    private int yearPublished;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="publisher_name", referencedColumnName="name", nullable = false)
    private Publisher publisher;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AUTHORING_ENTITY_EMAIL", referencedColumnName="email", nullable = false)
    private AuthoringEntity authoringEntity;

    public Book() {}

    public Book(String ISBN, String title, int yearPublished, Publisher publisher, AuthoringEntity ae) {
        this.setISBN(ISBN);
        this.setTitle(title);
        this.setYearPublished(yearPublished);
        this.setPublisher(publisher);
        this.setAuthoringEntity(ae);
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public AuthoringEntity getAuthoringEntity() {
        return authoringEntity;
    }

    public void setAuthoringEntity(AuthoringEntity authoringEntity) {
        this.authoringEntity = authoringEntity;
    }

    @Override
    public String toString() {
        return "ISBN: " + getISBN() + "\tTitle: " + getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            Book b = (Book) o;
            return this.ISBN.equals(b.ISBN);
        }
        return false;
    }
}
