package util.simpleGUI;

import javafx.scene.paint.Color;
import model.ReadOnlyWorld;

import javafx.scene.canvas.Canvas;

public class SimpleDraw {
    private int counter = 0;
    private ReadOnlyWorld w;
    Canvas c;
    public SimpleDraw(ReadOnlyWorld w, Canvas c) {
        this.c = c;
        this.w = w;
    }
    public void draw(ReadOnlyWorld w) {
        if (counter != 0) assert(w != this.w);
        c.getGraphicsContext2D().setFill(Color.BLACK);
        c.getGraphicsContext2D().fillRect(0,0,100 + counter,100);
//        Circle circle = new Circle();
//        circle.setCenterX(300);
//        circle.setCenterY(300);
//        circle.setRadius(50);
//        circle.setFill(Color.BLACK);
//        Pane p = new Pane();
//        p.getChildren().add(circle);
        counter++;
    }
}
