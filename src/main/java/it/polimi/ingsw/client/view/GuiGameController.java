package it.polimi.ingsw.client.view;

public class GuiGameController implements GuiController{
    private Gui gui;
    private ClientView view;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
        view = gui.getView();
    }

    public void initializeGame(){

    }
}
