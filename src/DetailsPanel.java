import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class DetailsPanel extends JPanel {
    private final JPanel detailsContainer;
    private final JLabel titleLabel;
    private final JTextArea detailsArea;

    //index
    private static final int TITLE_INDEX = 1;
    private static final int PLOT_INDEX = 5;
    private static final int RTSCORE_INDEX = 7;
    private static final int METACRITIC_INDEX = 9;
    private static final int IMDB_INDEX = 12;

    public DetailsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Film Details"));

        //create title label
        titleLabel = new JLabel("No film selected");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //create details text area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));

        //created details container
        detailsContainer = new JPanel(new BorderLayout());
        detailsContainer.add(titleLabel, BorderLayout.NORTH);
        detailsContainer.add(new JScrollPane(detailsArea), BorderLayout.CENTER);


        add(detailsContainer, BorderLayout.CENTER);


        clearDetails();
    }

    public void showDetails(String[] headers, Object[] data) {
        if (data == null || headers == null) {
            clearDetails();
            return;
        }

        //title film name
        if (data.length > TITLE_INDEX) {
            titleLabel.setText(data[TITLE_INDEX].toString());
        } else {
            titleLabel.setText("Unknown Title");
        }

        //build details
        StringBuilder sb = new StringBuilder();

        //add plot
        if (data.length > PLOT_INDEX) {
            sb.append("Plot: ").append(data[PLOT_INDEX].toString()).append("\n\n");
        }

        //key details
        for (int i = 0; i < headers.length && i < data.length; i++) {
            // Skip title and plot as they're already handled
            if (i != TITLE_INDEX && i != PLOT_INDEX) {
                sb.append(headers[i]).append(": ").append(data[i].toString());

                // Add special formatting for scores
                if (i == RTSCORE_INDEX || i == METACRITIC_INDEX) {
                    sb.append("%");
                } else if (i == IMDB_INDEX) {
                    sb.append("/10");
                }

                sb.append("\n\n");
            }
        }

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0); // Scroll to top
    }

    public void clearDetails() {
        titleLabel.setText("No film selected");
        detailsArea.setText("Select a film from the table to view details.");
    }
}