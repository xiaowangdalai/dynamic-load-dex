package com.example.dynamic_load_dex;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File outputFile = new File(getFilesDir(), "dynamicjar");
				
				DexClassLoader dcl = new DexClassLoader(dexPath, 
						getFilesDir().getAbsolutePath(), 
						null, 
						getClassLoader());
				
				
			}
		});
	}

}
