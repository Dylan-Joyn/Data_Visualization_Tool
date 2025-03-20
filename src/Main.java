import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        //read file
        String contents = Files.readString(Path.of("Pixar_Films/custom_pixar_tab_delimited.tsv"), StandardCharsets.UTF_8);

        //parse data
        List<String> lines = contents.lines().toList();
        String headerLine = lines.get(0);
        String[] headers = headerLine.split("\t");  // Split by tab instead of comma

        //arraylist for rows of data
        ArrayList<Object[]> data = new ArrayList<>();

        //parse data rows
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\t");

            data.add(parts);
        }
        // Run console test
        runConsole(headers, data);

        //Launch GUI
        SwingUtilities.invokeLater(() -> {
            PixarGUI gui = new PixarGUI(headers, data);
            gui.setVisible(true);
        });
    }

    private static void runConsole(String[] headers, List<Object[]> data) {
        //print attributes of first item
        System.out.println("Data item attributes");
        if (!data.isEmpty()) {
            Object[] firstItem = data.get(0);
            for (int i = 0; i < headers.length && i < firstItem.length; i++) {
                System.out.println(headers[i] + ": " + firstItem[i]);
            }
        } else {
            System.out.println("No data items found in file");
        }

        //print attributes of tenth item
        System.out.println("\nTenth data item attributes");
        if (data.size() >= 10) {
            Object[] tenthItem = data.get(9);
            for (int i = 0; i < headers.length && i < tenthItem.length; i++) {
                System.out.println(headers[i] + ": " + tenthItem[i]);
            }
        } else {
            System.out.println("File does not contain at least 10 data items");
        }

        //total entries
        System.out.println("\nTotal number of entries in the data: " + data.size());
    }
}