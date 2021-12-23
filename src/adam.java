import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 *	coding plan:
 *	☒ add peasycam
 *	☒ drawBlenderAxes()
 *	☒ add dialog box texture
 *	☐ dialogbox.java
 *		☐ how does JSON work in java
 *
 *		passage
 */
public class adam extends PApplet {
    PFont font;
    PeasyCam cam;
    PImage textFrame;
    JSONArray passages;

    // define the hue and saturation for all 3 axes
    final int X_HUE = 0, X_SAT = 80;
    final int Y_HUE = 90, Y_SAT = 80;
    final int Z_HUE = 210, Z_SAT = 80;
    final int DIM = 40; // brightness value for the dimmer negative axis
    final int BRIGHT = 75; // brightness value for the brighter positive axis

    public static void main(String[] args) {
        PApplet.main(new String[]{adam.class.getName()});
    }

    // let's load the JSON data
    public void loadData() {
        passages = loadJSONArray("data/passages.json");

        for (int i=0; i<passages.size(); i++) {
            System.out.println(passages.getJSONObject(i).getString("text"));
        }
    }

    @Override
    public void settings() {
        size(640, 360, P3D);
    }

    @Override
    public void setup() {
        rectMode(RADIUS);
        colorMode(HSB, 360f, 100f, 100f, 100f);
        frameRate(144);

        cam = new PeasyCam(this, 0, 0, 0, 500);
        font = createFont("data/gigamarujr.ttf", 14);
        textFont(font, 14);

        textFrame = loadImage("data/textFrame.png");
        loadData();
    }

    @Override
    public void draw() {
        background(234, 34, 24);
        drawBlenderAxes();

        cam.beginHUD();
        image(textFrame, 0, 0, width, height);
        cam.endHUD();
    }

    // draw axes in blender colors, with negative parts less bright
    public void drawBlenderAxes() {
        final int ENDPOINT = 10000;
        strokeWeight(1);

        // red x-axis
        stroke(X_HUE, X_SAT, DIM);
        line(-ENDPOINT, 0, 0, 0, 0, 0);
        stroke(X_HUE, X_SAT, BRIGHT);
        line(0, 0, 0, ENDPOINT, 0, 0);

        // green y axis
        stroke(Y_HUE, Y_SAT, DIM);
        line(0, -ENDPOINT, 0, 0, 0, 0);
        stroke(Y_HUE, Y_SAT, BRIGHT);
        line(0, 0, 0, 0, ENDPOINT, 0);

        // blue z axis
        stroke(Z_HUE, Z_SAT, DIM);
        line(0, 0, -ENDPOINT, 0, 0, 0);
        stroke(Z_HUE, Z_SAT, BRIGHT);
        line(0, 0, 0, 0, 0, ENDPOINT);
    }


    @Override
    public void mousePressed() {
        System.out.println(mouseX);
    }
}