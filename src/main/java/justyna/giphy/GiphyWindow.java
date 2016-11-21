package justyna.giphy;

import at.mukprojects.giphy4j.exception.GiphyException;
import com.sun.prism.*;
import com.sun.prism.image.ViewPort;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Justyna Salacinska
 */
public class GiphyWindow extends JFrame implements ActionListener {

    // Determine the JDBC URL. To connect the MySQL database
    private static final String URL;

    static {
        URL = "jdbc:mysql://localhost:3306/exercise?autoReconnect=true&useSSL=false";
    }

    private static final String USER = "root";
    //Enter the password in ""
    private static final String PASSWORD = "PLEASE ENTER THE PASSWORD FOR THE DB";
    private static final String TABLE = "giphy";
    // colums in table "giphy"
    private static final String ID_COLUMN = "id";
    private static final String LIKE_COLUMN = "likes";
    private static final String DISLIKE_COLUMN = "dislikes";

    // icons urls
    private static final String LIKE_URL = "https://image.freepik.com/free-icon/like_318-31404.jpg";
    private static final String DISLIKE_URL = "https://image.freepik.com/free-icon/dislike_318-31357.jpg";

    // size of buttons
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 50;
    private static final Dimension BUTTON_DIM = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);


    //variables
    private static Icon dislikeIcon;
    private static Icon likeIcon;

    private static GiphyConnector giphy;
    private static MySqlConnector mysql;


    //method for swing
    public GiphyWindow() {

        //Title,
        this.setTitle("Giphy browser");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 20);

        //Panel with label, buttons and JtextField
        jPanel1 = new JPanel();
        jPanel1.setPreferredSize(new Dimension(800, 50));
        jPanel1.setBackground(Color.LIGHT_GRAY);


        keywordTextField = new JTextField();
        searchButton = new JButton();
        numberOfResultsTextField = new JTextField("5");
        jLabel1 = new JLabel("number of gifs");
        gifScrollPane = new JScrollPane();
        gifPanel = new JPanel();


        searchButton.setText( "Search" );
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
                
            }


        } );



        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed( e );
            }
        } );


        // Layout for Jpanel1
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                        .addComponent(keywordTextField, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap( ComponentPlacement.RELATED)
                        .addComponent(searchButton)
                        .addPreferredGap( ComponentPlacement.RELATED, 357, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap( ComponentPlacement.RELATED)
                        .addComponent(numberOfResultsTextField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keywordTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchButton)
                                .addComponent(numberOfResultsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );


        // Layout for gifPanel
        gifPanel.setLayout(new GridBagLayout());
        gifScrollPane.setViewportView(gifPanel);




        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(gifScrollPane)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                     .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                     .addPreferredGap( ComponentPlacement.RELATED)
                     .addComponent(gifScrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
        );

        //unit increment ??
        gifScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        //sizes the frame + visible
        this.pack();
        this.setVisible(true);


    }

    private void searchButtonActionPerformed(ActionEvent evt) {
        gifPanel.removeAll();
        String text = this.keywordTextField.getText();
        int numberOfResults = Integer.parseInt( this.numberOfResultsTextField.getText() );

        try {
            GridBagConstraints constraints = new GridBagConstraints();

            // constrains fof GifPanel
            constraints.fill = GridBagConstraints.HORIZONTAL;

            Map<String, String> urls = giphy.searchForUrls(text, numberOfResults);
            int row = 0;
            for (Map.Entry<String, String> entry : urls.entrySet()) {
                String urlString = entry.getValue();
                String id = entry.getKey();

                JLabel gifLabel = new JLabel(new ImageIcon(new URL(urlString)));
                JButton likeButton = new JButton(likeIcon);
                JButton dislikeButton = new JButton(dislikeIcon);

                likeButton.setPreferredSize(BUTTON_DIM);
                likeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //global mysql
                        mysql.increaseLikes(id);



                    }
                });

                dislikeButton.setPreferredSize(BUTTON_DIM);
                dislikeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mysql.increaseDislikes(id);
                    }
                });


                // where to add (in row)
                constraints.gridy = row;
                //instets
                constraints.insets = new Insets(10, 0, 0, 0);

                // where to add in columns
                constraints.gridx = 1;
                gifPanel.add(gifLabel, constraints);

                constraints.insets = new Insets(0, 0, 0, 0);
                constraints.gridx = 2;
                gifPanel.add(likeButton, constraints);

                constraints.gridx = 3;
                gifPanel.add(dislikeButton, constraints);

                //repaint
                gifPanel.revalidate();
                                row++;
            }
        }

        //malformed URL (Oracele: Thrown to indicate that a malformed URL has occurred. Either no legal protocol could be found in a specification string or the string could not be parsed)
        catch (GiphyException | MalformedURLException ex) {

            /*A Logger object is used to log messages for a specific system or application component.
            Loggers are normally named, using a hierarchical dot-separated namespace.
            Logger names can be arbitrary strings, but they should normally be based on the package name or class name of the logged component,
            such as java.net or javax.swing. In addition it is possible to create "anonymous" Loggers that are not stored in the Logger namespace.
             */
            Logger.getLogger(GiphyWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void formWindowClosed(WindowEvent evt) throws Exception {
        try{
            mysql.close();
        } catch (SQLException ex) {
            Logger.getLogger( GiphyWindow.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    // resize gifs
    private static Image getScaledImage(Image scrImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g2.drawImage( scrImg, 0, 0, w, h, null );
        g2.dispose();

        return resizedImg;
    }
    //icons
    private static Icon getScaledIcon(String url, int width, int height) throws IOException {
        Image image = ImageIO.read(new URL( url ));
        image = getScaledImage( image, width, height );
        return new ImageIcon( image );
    }

    //main
    public static void main(String[] args) throws SQLException, IOException {
        likeIcon = getScaledIcon( LIKE_URL, BUTTON_WIDTH, BUTTON_HEIGHT );
        dislikeIcon = getScaledIcon( DISLIKE_URL, BUTTON_WIDTH, BUTTON_HEIGHT );

        giphy = new GiphyConnector();

        mysql = new MySqlConnector( URL, USER, PASSWORD, TABLE, ID_COLUMN, LIKE_COLUMN, DISLIKE_COLUMN );

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                new GiphyWindow().setVisible( true );
            }
        });


    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel gifPanel;
    private JScrollPane gifScrollPane;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JTextField keywordTextField;
    private JTextField numberOfResultsTextField;
    private JButton searchButton;

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    // End of variables declaration//GEN-END:variables



}
