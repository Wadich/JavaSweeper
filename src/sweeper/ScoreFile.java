package sweeper;

import java.io.*;
import java.util.*;


public class ScoreFile {
    private final String fileName = "Scoreboard.txt";
    private JavaTimer timer = new JavaTimer();
    HashMap<Long, String> dict = new HashMap<>();


    public void fileWrite(String text){
        try {
            FileWriter writer = new FileWriter(fileName, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text + "\n");
            bufferWriter.close();
            fileSort();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    void fileSort(){
        try {
            FileReader fr = new FileReader(fileName);
            Scanner scan = new Scanner(fr);
            String[] substr;
            while (scan.hasNextLine()) {
                substr = scan.nextLine().split(" ");
                long mils = timer.formatToMillis(substr[1]);
                dict.put(mils, substr[0]);
            }
            fr.close();
            Map<Long, String> treeMap = new TreeMap<>(dict);
            FileWriter writer = new FileWriter(fileName, false);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            for (Long key : treeMap.keySet()) {
                bufferWriter.write(treeMap.get(key) + " " + timer.millisToFormat(key) + "\n");
                System.out.println(treeMap.get(key) + " " + timer.millisToFormat(key));
            }

            bufferWriter.close();
            dict.clear();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
