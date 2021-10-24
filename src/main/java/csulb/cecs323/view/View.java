package csulb.cecs323.view;

import csulb.cecs323.app.*;


public class View {

    private BookClub bc;
    private UserInput user;

    public View(BookClub bc){
        this.bc = bc;
        this.user = new UserInput();
    }

    public void begin(){
        System.out.println("This is the main view method");
    }
}
