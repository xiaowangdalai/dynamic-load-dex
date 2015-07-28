package com.test.ajar;

public class DynamicTest implements IDynamic {

	@Override
	public String helloWorld() {
		return new AnotherClass().getString();
//		return "Hello 3 Jar!"; //aaa
	}

}
