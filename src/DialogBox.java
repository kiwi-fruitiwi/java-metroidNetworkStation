import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;


/** a dialog box with advancing text */
public class DialogBox {
    List<String> passages;
    List<Integer> durations;
    List<List<IndexPair>> highlightIndicesList;

    PApplet app;
    PImage textFrame; // Metroid dread's cyan bordered dialog frame

    String text; // our current passage text

    final int LEFT_MARGIN = 50;
    final int RIGHT_MARGIN = LEFT_MARGIN;
    final int BOTTOM_MARGIN = 10;
    final int HEIGHT = 120;

    int boxWidth;

    // TODO is there harm in initializing outside of the constructor?
    int index = 0; // character index we are currently displaying
    int passageIndex = 0; // which passage in our list are we displaying?

    public DialogBox(PApplet app,
                     List<String> passages,
                     List<Integer> durations,
                     List<List<IndexPair>> highlightIndicesList) {

        this.app = app;
        this.passages = passages;
        this.durations = durations;
        this.highlightIndicesList = highlightIndicesList;

        textFrame = app.loadImage("data/textFrame.png");
        text = this.passages.get(0);
        boxWidth = app.width;

        /*  TODO
                triangle
                time characters per section in Dread for advanceChar
                synchronize durations with video and audio
         */
    }


    public void renderTextFrame(PeasyCam cam) {
        cam.beginHUD();
        app.image(textFrame, 0, 0, app.width, app.height);
        cam.endHUD();
    }
}
