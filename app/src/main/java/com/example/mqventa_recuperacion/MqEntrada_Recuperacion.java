package com.example.mqventa_recuperacion;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MqEntrada_Recuperacion extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mq_entrada__recuperacion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mq_entrada__recuperacion, menu);
		return true;
	}

}
