import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TablePanel extends JPanel {
    private final String[] headers;
    private final List<Object[]> allData;
    private List<Object[]> filteredData;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private String yearFilter;
    private String ratingFilter;
    private String scoreFilter;

    //index for the data array
    private static final int TITLE_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    private static final int RATING_INDEX = 4;
    private static final int RTSCORE_INDEX = 7;

    public TablePanel(String[] headers, List<Object[]> data) {
        this.headers = headers;
        this.allData = data;
        this.filteredData = new ArrayList<>(allData);

        //make tabel model with data
        Object[][] tableData = filteredData.toArray(new Object[0][]);
        tableModel = new DefaultTableModel(tableData, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //make table with model
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //row sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);


        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Films"));
        add(new JScrollPane(table), BorderLayout.CENTER);

        //filters
        yearFilter = null;
        ratingFilter = null;
        scoreFilter = null;
    }

    public void addTableSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    public int getSelectedRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return -1;
        }
        return table.convertRowIndexToModel(viewRow);
    }

    public Object[] getSelectedData() {
        int modelRow = getSelectedRow();
        if (modelRow < 0 || modelRow >= filteredData.size()) {
            return null;
        }
        return filteredData.get(modelRow);
    }

    public List<Object[]> getFilteredData() {
        return filteredData;
    }

    public void filterByYear(String year) {
        yearFilter = year;
        applyFilters();
        refreshTable();
    }

    public void filterByRating(String rating) {
        ratingFilter = rating;
        applyFilters();
        refreshTable();
    }

    public void filterByScore(String score) {
        scoreFilter = score;
        applyFilters();
        refreshTable();
    }

    public void clearYearFilter() {
        yearFilter = null;
        applyFilters();
        refreshTable();
    }

    public void clearRatingFilter() {
        ratingFilter = null;
        applyFilters();
        refreshTable();
    }

    public void clearScoreFilter() {
        scoreFilter = null;
        applyFilters();
        refreshTable();
    }

    public void clearAllFilters() {
        yearFilter = null;
        ratingFilter = null;
        scoreFilter = null;
        filteredData = new ArrayList<>(allData);
        refreshTable();
    }

    private void applyFilters() {
        filteredData = new ArrayList<>();

        for (Object[] row : allData) {
            boolean passesYearFilter = true;
            boolean passesRatingFilter = true;
            boolean passesScoreFilter = true;

            //year filter logic
            if (yearFilter != null && row.length > YEAR_INDEX) {
                String releaseDate = row[YEAR_INDEX].toString();
                String[] dateParts = releaseDate.split("/");
                if (dateParts.length == 3) {
                    String year = dateParts[2];
                    // Convert 2-digit years to 4-digit years
                    if (year.length() == 2) {
                        int yearNum = Integer.parseInt(year);
                        year = (yearNum < 25) ? "20" + year : "19" + year;
                    }
                    passesYearFilter = year.equals(yearFilter);
                } else {
                    passesYearFilter = false;
                }
            }

            //rating filter logic
            if (ratingFilter != null && row.length > RATING_INDEX) {
                passesRatingFilter = row[RATING_INDEX].toString().equals(ratingFilter);
            }

            //score filter logic
            if (scoreFilter != null && row.length > RTSCORE_INDEX) {
                String scoreStr = row[RTSCORE_INDEX].toString();
                try {
                    int score = Integer.parseInt(scoreStr);
                    switch (scoreFilter) {
                        case "90-100":
                            passesScoreFilter = score >= 90 && score <= 100;
                            break;
                        case "80-89":
                            passesScoreFilter = score >= 80 && score < 90;
                            break;
                        case "70-79":
                            passesScoreFilter = score >= 70 && score < 80;
                            break;
                        case "Below 70":
                            passesScoreFilter = score < 70;
                            break;
                    }
                } catch (NumberFormatException e) {
                    passesScoreFilter = false;
                }
            }

            if (passesYearFilter && passesRatingFilter && passesScoreFilter) {
                filteredData.add(row);
            }
        }
    }

    private void refreshTable() {
        //clear data
        tableModel.setRowCount(0);

        //put filtered data to table
        for (Object[] row : filteredData) {
            tableModel.addRow(row);
        }

        table.clearSelection();
    }
}