import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String contents = Files.readString(Path.of("Pixar_Films/custom_pixar.csv"), StandardCharsets.UTF_8);

        List<String> lines = contents.lines().toList();
        String[] headers = lines.get(0).split(",");

        ArrayList<Object[]> data = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            data.add(lines.get(i).split(","));
        }

        // Run console test
        runConsole(headers, data);

        Object[][] allData = data.toArray(new Object[0][]);

        DefaultTableModel model = new DefaultTableModel(allData, headers);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Pixar Films");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(800, 800);
    }


    private static void runConsole(String[] headers, List<Object[]> data) {

        // 1. Print attributes of first data item
        System.out.println("Data item attributes");
        if (!data.isEmpty()) {
            Object[] firstItem = data.get(0);
            for (int i = 0; i < headers.length && i < firstItem.length; i++) {
                System.out.println(headers[i] + ": " + firstItem[i]);
            }
        } else {
            System.out.println("No data items found in file");
        }

        // 2. Print attributes of tenth data item
        System.out.println("\nTenth data item attributes");
        if (data.size() >= 10) {
            Object[] tenthItem = data.get(9);
            for (int i = 0; i < headers.length && i < tenthItem.length; i++) {
                System.out.println(headers[i] + ": " + tenthItem[i]);
            }
        } else {
            System.out.println("File does not contain at least 10 data items");
        }

        // 3. Display total number of entries
        System.out.println("\nTotal number of entries in the data: " + data.size());
    }
}