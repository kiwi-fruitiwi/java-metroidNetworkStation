import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *	coding plan:
 *	☒ add peasycam
 *	☒ drawBlenderAxes()
 *	☒ add dialog box texture
 *	🔧 dialogbox.java
 *		☒ how does JSON work in java
 *      ☒ add passage text: JSONObject.getString
 *      ☒ add durations: JSONObject.getInt
 *      ☒ add highlight indices: nested JSONObjects in JSONArray
 *  ☐ port elements of dialogbox
 *      ☐ display text
 *      ☐ wrap words
 *      ☐ highlights
 *      ☐ multiple passages with delay
 *  ☐ start adding adam's visualization
 *      ☐ spherical coordinates demo φ θ r! :D
 */
public class NetworkStation extends PApplet {
    PFont font;
    PeasyCam cam;
    JSONArray json;

    List<String> passages;
    List<Integer> durations;
    List<List<IndexPair>> highlightIndicesList;

    // define the hue and saturation for all 3 axes
    final int X_HUE = 0, X_SAT = 80;
    final int Y_HUE = 90, Y_SAT = 80;
    final int Z_HUE = 210, Z_SAT = 80;
    final int DIM = 40; // brightness value for the dimmer negative axis
    final int BRIGHT = 75; // brightness value for the brighter positive axis

    DialogBox dialog;

    public static void main(String[] args) {
        PApplet.main(new String[]{NetworkStation.class.getName()});
    }

    // let's load the JSON data
    public void loadData() {
        json = loadJSONArray("data/passages.json");
        passages = new ArrayList<>();
        durations = new ArrayList<>();
        highlightIndicesList = new ArrayList<>();
        JSONArray indexList;

        for (int i = 0; i< json.size(); i++) {
            JSONObject obj = json.getJSONObject(i);

            passages.add(obj.getString("text"));
            durations.add(obj.getInt("ms"));

            /*  grab highlight indices in this json format:
                "highlightIndices": [ ⭠ indexList
                    {"start": 52, "end": 66}, ⭠ indexPair
                    {"start": 195, "end": 211}
                ]
             */
            indexList = obj.getJSONArray("highlightIndices");
            JSONObject pair;

            // build array of indices that looks like [[52, 66][195, 111]]
            // outer list is "tupleList", inner is "tuple".
            // "tupleList" gets added to highlightIndicesList for each passage
            // highlightIndicesList ➜ [ [], [], [[52, 66][195, 111]], [] ]
            List<IndexPair> tupleList = new ArrayList<>();

            // iterate through highlights JSONArray and retrieve tuples
            for (int j = 0; j< indexList.size(); j++) {
                pair = indexList.getJSONObject(j);

                IndexPair p = new IndexPair(
                        pair.getInt("start"),
                        pair.getInt("end"));

                // 🌟 you can't just print an array. you'll get an object id
                // System.out.println(Arrays.toString(indices));
                tupleList.add(p);
            }
            highlightIndicesList.add(tupleList);
        }

        System.out.println(passages);
        System.out.println(durations);
        System.out.println(highlightIndicesList);
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

        loadData();
        dialog = new DialogBox(this, passages, durations, highlightIndicesList);
        cam = new PeasyCam(this, 0, 0, 0, 500);
        font = createFont("data/gigamarujr.ttf", 14);
        textFont(font, 14);
    }

    @Override
    public void draw() {
        background(234, 34, 24);
        drawBlenderAxes();

        dialog.renderTextFrame(cam);
        text(passages.get(0), 50, 100);
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