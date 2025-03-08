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
        String contents = Files.readString(Path.of("Pixar_Films/public_response.csv"), StandardCharsets.UTF_8);

        List<String> lines = contents.lines().toList();
        String[] headers = lines.get(0).split(",");

        ArrayList<Object[]> data = new ArrayList<>();

        for (int i =1; i < lines.size(); i++){
            data.add(lines.get(i).split(","));
        }

        Object[][] allData = data.toArray(new Object[0][]);

       // TableModel model = new DefaultTableModel(allData, headers);
        DefaultTableModel model = new DefaultTableModel(allData, headers);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);


        JFrame frame = new JFrame("Nobel Prizes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);


        frame.setSize(800, 800);



    }
}