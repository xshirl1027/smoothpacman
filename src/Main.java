import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * A panel maintaining a picture of a Do Not Enter sign.
 */
public class Main extends JPanel {
    private static final long serialVersionUID = 7148504528835036003L;

    /**
     * Called by the runtime system whenever the panel needs painting.
     */


    /**
     * A little driver in case you want a stand-alone application.
     */
    public static void main(String[] args) {
        var resourceName = "map.json";
        InputStream is = Main.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }
        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray map = object.getJSONArray("map");
        JSONArray temp = (JSONArray) map.get(0);
        int height = map.length();
        int width = temp.length();
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("A simple graphics program");
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            var pacman = new Pacman(600, 600, convertMapToArray(map));
            pacman.setBackground(Color.DARK_GRAY.darker());
            frame.getContentPane().add(pacman, BorderLayout.CENTER);
            frame.addKeyListener(new MovePacman(pacman));
            frame.setVisible(true);
        });
    }

   public static ArrayList<ArrayList<Integer>> convertMapToArray(JSONArray jsonMap){
        ArrayList<ArrayList<Integer>> map = new ArrayList<>();
        for(int i=0;i<jsonMap.length();i++){
            map.add(new ArrayList<>());
            JSONArray temp= (JSONArray)jsonMap.get(i);
            for(int j = 0; j<temp.length() ; j++)
            map.get(i).add((Integer) temp.get(j));
        }
        return map;
    }
}