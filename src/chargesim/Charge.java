package chargesim;

import javax.swing.*;

public class Charge {

    public double value;
    public int x;
    public int y;

    public ImageIcon positive = new ImageIcon("src/chargesim/dodatni.png");


    public Charge(int x, int y, double value) { //tutaj z argumentu usuniety Jpanel na ktorym to ma sie tworzyc
        this.x = x;
        this.y = y;
        this.value = value;
    }

}

