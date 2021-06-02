package com.travel.darktour_project;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialog {
    private Context context;
    public CustomDialog(Context context){
        this.context=context;
    }

    public void callFunction( TextView favoritecourse) {
        Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.map_dialog);
        dlg.show();

        Button cancleButton=(Button)dlg.findViewById(R.id.cancelButton);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"취소",Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
}
