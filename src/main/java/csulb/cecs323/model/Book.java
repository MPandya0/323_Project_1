package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniquePublisher", columnNames = {"title", "publisher"}),
        @UniqueConstraint(name = "UniqueAuthor", columnNames = {"title", "author"})
})
public class Book {
    @Id
    @Column(length = 17, nullable = false)
    private String ISBN;
    @Column(length = 80, nullable = false)
    private String title;
    @Column(nullable = false)
    private int yearPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_name", referencedColumnName = "name", nullable = false)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORING_ENTITY_NAME ", referencedColumnName = "email", nullable = false)
    private AuthoringEntity authoringEntity;

    public Book() {}

    public Book(String ISBN, String title, int yearPublished) {
        this.setISBN(ISBN);
        this.setTitle(title);
        this.setYearPublished(yearPublished);
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

    @Override
    public String toString() {
        return "Need to write toString() for Book";  // TODO - Need to write return string
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            Book b = (Book) o;
            return this.ISBN == b.ISBN;
        }
        return false;
    }
}
