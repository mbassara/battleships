package pl.mbassara.battleships;

import android.app.Activity;
import android.widget.TextView;

public class ShipsCounter {

	private TextView[] shipsCountTexts = new TextView[5];
	private String[] valuesStrings = new String[5];
	private int[] values = new int[5];
	
	public ShipsCounter(Activity parent) {
//        shipsCountTexts[4] = (TextView) parent.findViewById(R.id.aircraft_carriers_value_text);	// TODO
//        shipsCountTexts[3] = (TextView) parent.findViewById(R.id.battleships_value_text);
//        shipsCountTexts[2] = (TextView) parent.findViewById(R.id.submarines_value_text);
//        shipsCountTexts[1] = (TextView) parent.findViewById(R.id.destroyers_value_text);
//        shipsCountTexts[0] = (TextView) parent.findViewById(R.id.patrol_boats_value_text);
        
        valuesStrings[4] = (String) parent.getResources().getString(R.string.aircraft_carriers_value);
        valuesStrings[3] = (String) parent.getResources().getString(R.string.battleships_value);
        valuesStrings[2] = (String) parent.getResources().getString(R.string.submarines_value);
        valuesStrings[1] = (String) parent.getResources().getString(R.string.destroyers_value);
        valuesStrings[0] = (String) parent.getResources().getString(R.string.patrol_boats_value);

        for(int i = 0; i < 5; i++)
        	values[i] = 0;
	}
	
	public boolean canAddShip(int shipType) {
		return shipType >= 1 && shipType <= 5 && values[shipType-1] < (6 - shipType);
	}
	
	public boolean addShip(int shipType) {
		if(!canAddShip(shipType))
			return false;
		else {
			values[shipType-1]++;
			shipsCountTexts[shipType-1].setText(values[shipType-1] + " " + valuesStrings[shipType-1]);
			return true;
		}
	}
	
	public boolean removeShip(int shipType) {
		if(shipType > 0 && values[shipType-1] > 0) {
			values[shipType-1]--;
			shipsCountTexts[shipType-1].setText(values[shipType-1] + " " + valuesStrings[shipType-1]);
			return true;
		}
		else
			return false;
	}
}
