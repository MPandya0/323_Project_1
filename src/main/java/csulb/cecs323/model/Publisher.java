package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@NamedNativeQuery(
        name = "FindPublisherUsingPhone",
        query = "Select P.* From Publisher P WHERE P.phone = ? ",
        resultClass = Publisher.class
)
@NamedNativeQuery(
        name = "FindPublisherUsingEmail",
        query = "Select P.* From Publisher P Where P.email = ? ",
        resultClass = Publisher.class
)
@NamedNativeQuery(
        name = "FindPublisherUsingName",
        query = "Select P.* From Publisher P Where P.name = ?1",
        resultClass = Publisher.class
)

public class Publisher {

    @Id
    @Column(length = 80, nullable = false)
    /**The name of the publisher*/
    private String name;
    @Column(length = 80, nullable = false, unique = true)
    /**The publisher email address*/
    private String email;
    @Column(length = 24, nullable = false, unique = true)
    /**The publishers phone number*/
    private String phone;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.PERSIST)
    private List<Book> books = new ArrayList<Book>();

    /**
     * Default constructor
     */
    public Publisher() {}

    /**
     * Constructs a publisher object.
     *
     * @param name - The name of the publisher
     * @param email - The publisher email address
     * @param phone - The publishers phone number
     */
    public Publisher(String name, String email, String phone) {
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Publisher Name: " + this.getName() +
                " Email Address: " + this.getEmail() +
                " Phone Number: " + this.getPhone();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Publisher) {
            Publisher p = (Publisher) o;
            return this.name.equals(p.name);
        }
        return false;
    }
}
