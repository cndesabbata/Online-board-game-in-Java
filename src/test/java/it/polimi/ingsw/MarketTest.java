package it.polimi.ingsw;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.MarketSelection;
import org.junit.Test;

public class MarketTest {
    @Test
    public void printMarket() {
        Market market = new Market();
        Marble[][] disposition = market.getDisposition();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(disposition[i][j] + ", ");
            }
            System.out.print("\n");
        }
        System.out.println(market.getExternal());
        market.setDisposition(MarketSelection.COLUMN, 4);
        disposition = market.getDisposition();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(disposition[i][j] + ", ");
            }
            System.out.print("\n");
        }
        System.out.println(market.getExternal());
        assertTrue(true);
    }
}
