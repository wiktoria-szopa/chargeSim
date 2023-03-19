package chargesim;
import javax.swing.*;

    public class Charge {

        public int value;
        public int x;
        public int y;

        ImageIcon dodatni = new ImageIcon("images/dodatni.png");



        public Charge() { //tutaj z argumentu usuniety Jpanel na ktorym to ma sie tworzyc
            value = 1;
            x = 200;
            y = 200;
            /*
            x = panel.getWidth()/2;
            y = panel.getHeight()/2;*/
        }

    }

