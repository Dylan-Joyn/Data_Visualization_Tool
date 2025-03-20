import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {
    private final String[] headers;
    private final JLabel totalFilmsLabel;
    private final JLabel avgRTScoreLabel;
    private final JLabel avgIMDBScoreLabel;
    private final JLabel highestRatedLabel;
    private final JLabel avgRuntimeLabel;

    // Indices for the data array based on the tab-delimited structure
    private static final int TITLE_INDEX = 1;
    private static final int RUNTIME_INDEX = 3;
    private static final int RTSCORE_INDEX = 7;
    private static final int IMDBSCORE_INDEX = 12;

    public StatsPanel(String[] headers, List<Object[]> data) {
        this.headers = headers;

        //create labels
        totalFilmsLabel = new JLabel();
        avgRTScoreLabel = new JLabel();
        avgIMDBScoreLabel = new JLabel();
        highestRatedLabel = new JLabel();
        avgRuntimeLabel = new JLabel();

        setLayout(new GridLayout(5, 1));
        setBorder(BorderFactory.createTitledBorder("Statistics"));

        //add compenents with labels
        add(createLabelPanel("Total Films:", totalFilmsLabel));
        add(createLabelPanel("Avg. RT Score:", avgRTScoreLabel));
        add(createLabelPanel("Avg. IMDB Score:", avgIMDBScoreLabel));
        add(createLabelPanel("Highest Rated Film:", highestRatedLabel));
        add(createLabelPanel("Avg. Runtime (min):", avgRuntimeLabel));

        // Initial update
        updateStats(data);
    }

    private JPanel createLabelPanel(String labelText, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(labelText), BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    public void updateStats(List<Object[]> data) {
        //statistics calculations
        int totalFilms = data.size();

        double totalRTScore = 0;
        int rtScoreCount = 0;

        double totalIMDBScore = 0;
        int imdbScoreCount = 0;

        int totalRuntime = 0;
        int runtimeCount = 0;

        Object[] highestRatedFilm = null;
        double maxRTScore = -1;

        for (Object[] row : data) {
            //rotten tomatoe Score
            if (row.length > RTSCORE_INDEX) {
                try {
                    double rtScore = Double.parseDouble(row[RTSCORE_INDEX].toString());
                    totalRTScore += rtScore;
                    rtScoreCount++;

                    //highest rated film tracking
                    if (rtScore > maxRTScore && row.length > TITLE_INDEX) {
                        maxRTScore = rtScore;
                        highestRatedFilm = row;
                    }
                } catch (NumberFormatException e) {
                    //skip if not a valid number
                }
            }

            //IMDB Score
            if (row.length > IMDBSCORE_INDEX) {
                try {
                    double imdbScore = Double.parseDouble(row[IMDBSCORE_INDEX].toString());
                    totalIMDBScore += imdbScore;
                    imdbScoreCount++;
                } catch (NumberFormatException e) {
                    //skip if not a valid number
                }
            }

            //Runtime
            if (row.length > RUNTIME_INDEX) {
                try {
                    int runtime = Integer.parseInt(row[RUNTIME_INDEX].toString());
                    totalRuntime += runtime;
                    runtimeCount++;
                } catch (NumberFormatException e) {
                    //skip if not a valid number
                }
            }
        }

        //average calculations
        double avgRTScore = rtScoreCount > 0 ? totalRTScore / rtScoreCount : 0;
        double avgIMDBScore = imdbScoreCount > 0 ? totalIMDBScore / imdbScoreCount : 0;
        double avgRuntime = runtimeCount > 0 ? (double) totalRuntime / runtimeCount : 0;

        //update labels
        totalFilmsLabel.setText(String.valueOf(totalFilms));
        avgRTScoreLabel.setText(String.format("%.1f%%", avgRTScore));
        avgIMDBScoreLabel.setText(String.format("%.1f/10", avgIMDBScore));
        avgRuntimeLabel.setText(String.format("%.1f", avgRuntime));

        if (highestRatedFilm != null) {
            highestRatedLabel.setText(highestRatedFilm[TITLE_INDEX].toString() +
                    " (" + String.format("%.0f%%", maxRTScore) + ")");
        } else {
            highestRatedLabel.setText("N/A");
        }
    }
}