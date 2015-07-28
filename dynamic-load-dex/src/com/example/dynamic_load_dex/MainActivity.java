package com.example.dynamic_load_dex;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import com.test.ajar.DynamicTest;

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
	
	private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (i % 2 != 0) {
//					Toast.makeText(MainActivity.this, new DynamicTest().helloWorld(), Toast.LENGTH_LONG).show();
//				} else {
					File outFile = new File(getFilesDir().getAbsolutePath() + "/dynamic-jar-test");
					
					if (!outFile.isDirectory()) {
						outFile.mkdirs();
					}
					
					// 每次加载前，需要删除临时dex文件，否则当jar文件有变化且进程没退出时，会force close
					if (outFile.listFiles() != null) {
						for (File f : outFile.listFiles()) {
							if (f.isFile()) {
								f.delete();
							}
						}
					}
					
					/*
					 * 需要添加系统权限以保证所有手机可用
					 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
					 */
					File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
							+ DEX_PATH);
					
					/*
					 * 1、如果加载的类中含有其他类的调用，同样可以正常运行，说明加载某个类的时候会把相关
					 * 的类也加载进来
					 * 2、加载的时候如果optimizedDirectory下已经有提取出的dex文件，那么二次加载的时候，
					 * 如果原始jar已发生变化（被覆盖），可能会抛出未知异常，而且dex文件将被系统删除
					 * 3、不能把jar集成进来，否则load不成功
					 */
					ClassLoader cl = new DexClassLoader(file.getAbsolutePath(), 
							getFilesDir().getAbsolutePath() + "/dynamic-jar-test", 
							null, 
							getClassLoader());
					
					try {
						Class<?> libProviderClazz = cl.loadClass("com.test.ajar.DynamicTest");
						Object obj = libProviderClazz.newInstance();
						Method method = libProviderClazz.getMethod("helloWorld", new Class<?>[] {});
//						method.setAccessible(true);
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
				
//				i++;
//			}
		});
	}

}
