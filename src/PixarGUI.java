import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PixarGUI extends JFrame {
    private String[] headers;
    private List<Object[]> data;
    private TablePanel tablePanel;
    private DetailsPanel detailsPanel;
    private StatsPanel statsPanel;
    private ChartPanel chartPanel;
    private JPanel filterPanel;

    //index for the data array
    private static final int TITLE_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    private static final int RATING_INDEX = 4;

    public PixarGUI(String[] headers, List<Object[]> data) {
        super("Pixar Films Analysis");
        this.headers = headers;
        this.data = data;

        //frame set up
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen

        //panel creation
        createPanels();

        //layout set up
        setupLayout();
    }

    private void createPanels() {
        //table panel creation
        tablePanel = new TablePanel(headers, data);
        tablePanel.addTableSelectionListener(e -> {
            int selectedRow = tablePanel.getSelectedRow();
            if (selectedRow >= 0) {
                detailsPanel.showDetails(headers, tablePanel.getSelectedData());
            }
        });

        //details panel
        detailsPanel = new DetailsPanel();

        //stats panel
        statsPanel = new StatsPanel(headers, data);

        //chart panel
        chartPanel = new ChartPanel(headers, data);

        //filter panel
        filterPanel = createFilterPanel();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Filters"));

        //get years
        Set<String> uniqueYears = new TreeSet<>();
        for (Object[] row : data) {
            if (row.length > YEAR_INDEX) {
                String releaseDate = row[YEAR_INDEX].toString();
                String[] dateParts = releaseDate.split("/");
                if (dateParts.length == 3) {
                    String year = dateParts[2];
                    //turn 2 digit years to 4 digit
                    if (year.length() == 2) {
                        int yearNum = Integer.parseInt(year);
                        //assume 00-24 is 2000-2024, 25-99 is 1925-1999
                        year = (yearNum < 25) ? "20" + year : "19" + year;
                    }
                    uniqueYears.add(year);
                }
            }
        }
        String[] years = uniqueYears.toArray(new String[0]);

        //get unique ratings
        Set<String> uniqueRatings = new TreeSet<>();
        for (Object[] row : data) {
            if (row.length > RATING_INDEX) {
                String rating = row[RATING_INDEX].toString();
                uniqueRatings.add(rating);
            }
        }
        String[] ratings = uniqueRatings.toArray(new String[0]);

        //scores for Rotten Tomatoes data
        String[] scoreRanges = {"90-100", "80-89", "70-79", "Below 70"};

        //filter for years
        JComboBox<String> yearFilter = new JComboBox<>(years);
        yearFilter.insertItemAt("All Years", 0);
        yearFilter.setSelectedIndex(0);

        //rating filter
        JComboBox<String> ratingFilter = new JComboBox<>(ratings);
        ratingFilter.insertItemAt("All Ratings", 0);
        ratingFilter.setSelectedIndex(0);

        //score filter
        JComboBox<String> scoreFilter = new JComboBox<>(scoreRanges);
        scoreFilter.insertItemAt("All Scores", 0);
        scoreFilter.setSelectedIndex(0);

        //action listeners
        yearFilter.addActionListener(e -> {
            String selectedYear = (String) yearFilter.getSelectedItem();
            if ("All Years".equals(selectedYear)) {
                tablePanel.clearYearFilter();
            } else {
                tablePanel.filterByYear(selectedYear);
            }
            updateAllPanels();
        });

        ratingFilter.addActionListener(e -> {
            String selectedRating = (String) ratingFilter.getSelectedItem();
            if ("All Ratings".equals(selectedRating)) {
                tablePanel.clearRatingFilter();
            } else {
                tablePanel.filterByRating(selectedRating);
            }
            updateAllPanels();
        });

        scoreFilter.addActionListener(e -> {
            String selectedScore = (String) scoreFilter.getSelectedItem();
            if ("All Scores".equals(selectedScore)) {
                tablePanel.clearScoreFilter();
            } else {
                tablePanel.filterByScore(selectedScore);
            }
            updateAllPanels();
        });

        //clear filter buttons
        JButton clearButton = new JButton("Clear All Filters");
        clearButton.addActionListener(e -> {
            yearFilter.setSelectedIndex(0);
            ratingFilter.setSelectedIndex(0);
            scoreFilter.setSelectedIndex(0);
            tablePanel.clearAllFilters();
            updateAllPanels();
        });

        //add to panel
        panel.add(new JLabel("Year:"));
        panel.add(yearFilter);
        panel.add(new JLabel("Rating:"));
        panel.add(ratingFilter);
        panel.add(new JLabel("RT Score:"));
        panel.add(scoreFilter);
        panel.add(clearButton);

        return panel;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        //top panel
        add(filterPanel, BorderLayout.NORTH);

        //left scrollpanel
       JScrollPane tableScrollPane = new JScrollPane(tablePanel);

        //right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(detailsPanel, BorderLayout.CENTER);

        //bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(statsPanel);
        bottomPanel.add(chartPanel);

        //split panes
        JSplitPane verticalSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tableScrollPane,
                bottomPanel
        );
        verticalSplit.setResizeWeight(0.6);

        JSplitPane horizontalSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                verticalSplit,
                rightPanel
        );
        horizontalSplit.setResizeWeight(0.7);

        //center splitpane
        add(horizontalSplit, BorderLayout.CENTER);
    }

    private void updateAllPanels() {
        List<Object[]> filteredData = tablePanel.getFilteredData();
        statsPanel.updateStats(filteredData);
        chartPanel.updateChart(filteredData);
        //if no row is selected after filter, clear data
        if (tablePanel.getSelectedRow() < 0) {
            detailsPanel.clearDetails();
        }
    }
}