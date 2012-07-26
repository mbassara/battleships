package pl.mbassara.battleships;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Board extends TableLayout {
	
	public static final int SIZE_SMALL = 1;
	public static final int SIZE_BIG = 2;
	
	protected TableRow[] rows = new TableRow[10];
	
	public Board(Context context) {
		super(context);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
														LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.setLayoutParams(layoutParams);
        
        for(int i = 0; i < 10; i++) {
        	rows[i] = new TableRow(context);
        	rows[i].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        	this.addView(rows[i]);
        }
		
	}
}
