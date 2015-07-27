package com.example.dynamic_load_dex;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {
	
	// 把jar文件手动放到sdcard的这个目录下
	private static final String DEX_PATH = "/dynamic-jar-test/ajar.jar";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File outFile = new File(getFilesDir().getAbsolutePath() + "/dynamic-jar-test");
				
				if (!outFile.isDirectory()) {
					outFile.mkdirs();
				}
				
				// 每次加载前，需要删除临时dex文件，否则当jar文件有变化时，会force close
				if (outFile.listFiles() != null) {
					for (File f : outFile.listFiles()) {
						if (f.isFile()) {
							f.delete();
						}
					}
				}
				
				File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
						+ DEX_PATH);
				
				ClassLoader cl = new DexClassLoader(file.getAbsolutePath(), 
						getFilesDir().getAbsolutePath() + "/dynamic-jar-test", 
						null, 
						getClassLoader());
				
				try {
					Class<?> libProviderClazz = cl.loadClass("com.test.ajar.DynamicTest");
					Object obj = libProviderClazz.newInstance();
					Method method = libProviderClazz.getMethod("helloWorld", new Class<?>[] {});
//					method.setAccessible(true);
					String str = (String) method.invoke(obj, new Object[] {});
					
					Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

}
