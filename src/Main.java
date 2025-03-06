import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String contents = Files.readString(Path.of("nobel_prizes.csv"), StandardCharsets.UTF_8);

        List<String> lines = List.of(contents.split("\n"));
        ArrayList<Object[]> data = new ArrayList<>();
        data = lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        Object[][] allData = data.toArray(new Object[data.size()][]);

        TableModel model = new DefaultTableModel(allData, 4);

        JTable table = new JTable(model);

        JPanel panel = new JPanel();
        panel.add(table);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

    }
}