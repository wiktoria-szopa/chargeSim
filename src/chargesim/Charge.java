package chargesim;

import javax.swing.*;

public class Charge {
    private double value;
    private double x;
    private double y;

    public Charge(double x, double y, double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    //start get/set region
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	//end get/set region

}

