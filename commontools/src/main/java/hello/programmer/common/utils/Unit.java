package hello.programmer.common.utils;

public class Unit<A> {
	A value;

	public Unit(A value) {
		super();
		this.value = value;
	}

	public A getValue() {
		return value;
	}

	public void setValue(A value) {
		this.value = value;
	}
}
