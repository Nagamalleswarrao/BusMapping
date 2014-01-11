package com.creative.busmapping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.creative.busmapping.R;

/**
 * Created by Eshwar on 1/5/14.
 */
public class InputActivity extends Activity {


    EditText mSourceEdit, mDestinationEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button buttDirections = (Button) findViewById(R.id.buttDirections);
        Button buttBusses = (Button) findViewById(R.id.buttbusses);
        mSourceEdit = (EditText) findViewById(R.id.editTextSource);
        Bundle args = getIntent().getExtras();
        String src = "none";
        if(args != null){
            src = args.getString("BUS_STOP");
        }
        mSourceEdit.setText(src);
        mDestinationEdit = (EditText) findViewById(R.id.editTextDestination);


        buttDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InputActivity.this, DirectionsActivity.class);
                intent.putExtra("SOURCE", mSourceEdit.getText().toString());
                intent.putExtra("DESTINATION",mDestinationEdit.getText().toString());
                intent.putExtra("bundle", getIntent().getExtras());
                startActivity(intent);
            }
        });

        buttBusses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InputActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
