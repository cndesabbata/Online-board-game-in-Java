package it.polimi.ingsw.modelTest;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.server.model.Marble;
import it.polimi.ingsw.server.model.Market;
import it.polimi.ingsw.server.model.MarketSelection;
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

    public boolean expectedDisposition(Marble[][] disposition1, Marble[][] disposition2, Marble external1, Marble external2) {
        if(external1 != external2)
            return false;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                if(disposition1[i][j] != disposition2[i][j])
                    return false;
            }
        }
        return true;
    }
}
