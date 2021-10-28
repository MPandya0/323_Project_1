package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Writing Group")
public class WritingGroup extends AuthoringEntity {
    @Column(length = 80)
    private String headWriter;
    @Column
    private int yearFormed;

    public WritingGroup() {}

    public WritingGroup(String headWriter, int yearFormed, String email, String name) {
        super(email, name);
        this.setHeadWriter(headWriter);
        this.setYearFormed(yearFormed);
    }

    public String getHeadWriter() {
        return headWriter;
    }

    public void setHeadWriter(String headWriter) {
        this.headWriter = headWriter;
    }

    public int getYearFormed() {
        return yearFormed;
    }

    public void setYearFormed(int yearFormed) {
        this.yearFormed = yearFormed;
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
