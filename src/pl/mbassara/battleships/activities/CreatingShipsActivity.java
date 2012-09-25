package pl.mbassara.battleships.activities;

import pl.mbassara.battleships.activities.GameActivity;
import pl.mbassara.battleships.AIComputer;
import pl.mbassara.battleships.Board;
import pl.mbassara.battleships.Global;
import pl.mbassara.battleships.CreatingShipsBoard;
import pl.mbassara.battleships.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.RelativeLayout.LayoutParams;

public class CreatingShipsActivity extends Activity{

	private static CreatingShipsBoard board = null;
	private final CreatingShipsActivity context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_ships);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.creating_ships_layout);
        board = new CreatingShipsBoard(this, Board.SIZE_BIG);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        board.setLayoutParams(layoutParams);
        layout.addView(board);
    }
    
    public void togglePlacingShipsMode(View view){
    	boolean successfull = board.togglePlacingShipsMode((ToggleButton) view);
    	if(successfull) {
        	((Button) findViewById(R.id.next_button)).setEnabled(!((ToggleButton) view).isChecked());
        	((Button) findViewById(R.id.create_random_button)).setEnabled(((ToggleButton) view).isChecked());
    	}
    }
    
    public void random(View view){
    	board.setBoard(AIComputer.generateMatrix());
    }
    
    public void next(View view) {
    	Global.USERS_NAME = getString(R.string.your_score);
//   		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//   		View layout = inflater.inflate(R.layout.dialog_user_name,
//   										(ViewGroup) findViewById(R.id.userNameDialogLayout));
//        AlertDialog dialog;
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(layout)
//				.setCancelable(false)
//				.setTitle(R.string.enter_name)
//				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface dialog, int which) {
//						EditText textField = (EditText) ((AlertDialog) dialog).findViewById(R.id.userNameDialogEditText);
//						String name = textField.getText().toString();
//						
//						if(name.length() > 5)
//							Global.USERS_NAME = name.substring(0, 5);
//						else
//							Global.USERS_NAME = name;
						
				    	Intent intent;
				   		intent = new Intent(context, GameActivity.class);
				   		
				    	context.startActivity(intent);
				    	((CreatingShipsActivity) context).finish();
//					}
//				});
//        
//        dialog = builder.create();
//        Toast.makeText(this, R.string.enter_name_info, Toast.LENGTH_LONG);
//        dialog.show();
	}
    
    public static boolean[][] getBoardMatrix() {
    	return board.getBoardMatrix();
    }
}
