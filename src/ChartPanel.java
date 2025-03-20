import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {
    private final String[] headers;
    private List<Object[]> data;

    //index for the data array
    private static final int YEAR_INDEX = 2;
    private static final int RTSCORE_INDEX = 7;

    private final JPanel chartContainer;

    public ChartPanel(String[] headers, List<Object[]> data) {
        this.headers = headers;
        this.data = data;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Rotten Tomatoes Scores by Year"));

        //chart container
        chartContainer = new JPanel();
        chartContainer.setLayout(new BorderLayout());
        add(chartContainer, BorderLayout.CENTER);

        updateChart(data);
    }

    public void updateChart(List<Object[]> newData) {
        this.data = newData;

        //group data by year and calculate average Rotten Tomatoes score
        Map<String, List<Integer>> scoresByYear = new TreeMap<>();

        for (Object[] row : data) {
            if (row.length > YEAR_INDEX && row.length > RTSCORE_INDEX) {
                //extract year from release date
                String releaseDate = row[YEAR_INDEX].toString();
                String[] dateParts = releaseDate.split("/");
                String year = "Unknown";

                if (dateParts.length == 3) {
                    year = dateParts[2];
                    //tun 2 digit to 4 digit format
                    if (year.length() == 2) {
                        int yearNum = Integer.parseInt(year);
                        year = (yearNum < 25) ? "20" + year : "19" + year;
                    }
                }

                try {
                    int score = Integer.parseInt(row[RTSCORE_INDEX].toString());
                    if (!scoresByYear.containsKey(year)) {
                        scoresByYear.put(year, new ArrayList<>());
                    }
                    scoresByYear.get(year).add(score);
                } catch (NumberFormatException e) {
                    //skip if not a valid number
                }
            }
        }

        //calculate average score by year
        Map<String, Integer> avgScoresByYear = new TreeMap<>();
        for (Map.Entry<String, List<Integer>> entry : scoresByYear.entrySet()) {
            int sum = 0;
            List<Integer> scores = entry.getValue();
            for (int score : scores) {
                sum += score;
            }
            int avg = scores.isEmpty() ? 0 : sum / scores.size();
            avgScoresByYear.put(entry.getKey(), avg);
        }

        //bar chart
        chartContainer.removeAll();
        chartContainer.add(createBarChart(avgScoresByYear), BorderLayout.CENTER);

        //refresh UI
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private JPanel createBarChart(Map<String, Integer> data) {
        //find the maximum value for scaling
        double maxValue = 100;

        //chart panel
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                //chart axes
                g2d.setColor(Color.BLACK);
                g2d.drawLine(50, height - 50, width - 20, height - 50); // x-axis
                g2d.drawLine(50, height - 50, 50, 20); // y-axis

                //bar width and spacing
                int barCount = data.size();
                int barWidth = Math.max(10, (width - 80) / Math.max(1, barCount));
                int barSpacing = 5;

                //draw bars
                int x = 60;
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    String year = entry.getKey();
                    int score = entry.getValue();

                    //bar height
                    int barHeight = (int) ((score / maxValue) * (height - 100));

                    Color barColor = new Color(65, 105, 225); // Royal Blue

                    //draw bar
                    g2d.setColor(barColor);
                    g2d.fillRect(x, height - 50 - barHeight, barWidth - barSpacing, barHeight);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, height - 50 - barHeight, barWidth - barSpacing, barHeight);

                    //put score on top of bar
                    g2d.setFont(new Font("Arial", Font.BOLD, 10));
                    String scoreText = Integer.toString(score);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(scoreText);
                    g2d.drawString(scoreText, x + (barWidth - barSpacing - textWidth) / 2, height - 50 - barHeight - 5);

                    //year label
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    FontMetrics metrics = g2d.getFontMetrics();
                    int labelWidth = metrics.stringWidth(year);
                    g2d.drawString(year, x + (barWidth - barSpacing - labelWidth) / 2, height - 35);

                    x += barWidth;
                }

                //y axis labels
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                int steps = 5;
                for (int i = 0; i <= steps; i++) {
                    double value = (maxValue / steps) * i;
                    int y = height - 50 - (int) ((value / maxValue) * (height - 100));
                    String label = String.format("%.0f%%", value);
                    g2d.drawString(label, 5, y + 5);
                    g2d.drawLine(45, y, 50, y); // Tick mark
                }

                //title
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics metrics = g2d.getFontMetrics();
                String title = "Average Rotten Tomatoes Score by Year";
                int titleWidth = metrics.stringWidth(title);
                g2d.drawString(title, (width - titleWidth) / 2, 15);
            }
        };

        chart.setPreferredSize(new Dimension(400, 300));

        return chart;
    }

}