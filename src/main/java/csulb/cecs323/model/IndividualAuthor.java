package csulb.cecs323.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("Individual Author")
public class IndividualAuthor extends AuthoringEntity {

    @ManyToMany(mappedBy="individualAuthorList")
    List<AdHocTeam> adHocTeamList;

    public IndividualAuthor(){}

    public IndividualAuthor(String email, String name) {
        super(email, name);
    }

    public void insertAdHocTeam(AdHocTeam adHocTeam) {
        adHocTeamList.add(adHocTeam);
    }

    public boolean removeAdHocTeam(AdHocTeam adHocTeam) {
        return adHocTeamList.remove(adHocTeam);
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
