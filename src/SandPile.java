import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")

public class SandPile extends JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();      //fetch screen dimensions
    private static int DIMX = 720, DIMY = 720;
    private static double LOCX = (screenSize.getWidth() - DIMX) / 2, LOCY = (screenSize.getHeight() - DIMY) / 2;

    private static Color color0 = Color.WHITE;

    //Instagram colors
    private static Color color1 = new Color(233, 89, 80);
    private static Color color2 = new Color(251, 173, 80);
    private static Color color3 = new Color(138, 58, 185);

    private static int startAmount = 1000000;       //number of grains
    private static int threshold = 4;

    private static int frequency = 100;  //how many cycles before I draw the result
    private int count = 0;
    private int screenCount = 0;

    private Integer realRadius;     //used to work on the right portion of the canvas

    private int[][] grid = new int[DIMX][DIMY];
    private int[][] oldGrid = new int[DIMX][DIMY];

    private JPanel mainPanel;

    public SandPile(String title) {

        super(title);
        this.setSize(DIMX, DIMY);
        this.setLocation((int) LOCX, (int) LOCY);

        this.getContentPane().setBackground(color0);
        this.setBackground(color0);

        //initializing the grid
        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {
                grid[x][y] = 0;
                oldGrid[x][y] = 0;
            }
        }
        grid[DIMX / 2][DIMY / 2] = startAmount;
        realRadius = 1;

        mainPanel = new JPanel() {      //override of JPanel to be able to draw
            @Override
            public void paintComponent(Graphics g) {

                //super.paintComponent(g);

                int topLeftX = (DIMX / 2 - realRadius);
                int topLeftY = (DIMY / 2 - realRadius);
                int downRightX = (DIMX / 2 + realRadius);
                int downRightY = (DIMY / 2 + realRadius);

                for (int x = topLeftX; x <= downRightX; x++) {
                    for (int y = topLeftY; y <= downRightY; y++) {
                        if (grid[x][y] > 0) {
                            g.setColor(selectColor(grid[x][y]));
                            g.drawLine(x, y, x, y);         //draw a single dot
                        }
                    }
                }
            }
        };

        mainPanel.setBackground(color0);

        this.add(mainPanel);

        this.setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void SandSlide() {
        int absDistance;

        oldGrid = grid;
        grid = new int[DIMX][DIMY];

        //I just work on the minimum rectangle that contains the draw
        int topLeftX = (DIMX / 2 - realRadius);
        int topLeftY = (DIMY / 2 - realRadius);
        int downRightX = (DIMX / 2 + realRadius);
        int downRightY = (DIMY / 2 + realRadius);

        //I set to zero all the pixels of the new grid (just the rectangle I work on)
        for (int x = topLeftX - 1; x <= downRightX + 1; x++) {
            for (int y = topLeftY - 1; y <= downRightY + 1; y++) {
                grid[x][y] = 0;
            }
        }

        for (int x = topLeftX; x <= downRightX; x++) {
            for (int y = topLeftY; y <= downRightY; y++) {

                if (oldGrid[x][y] < threshold)
                    grid[x][y] += oldGrid[x][y];
                else {                                            //here comes the slide
                    grid[x][y] += (oldGrid[x][y] - threshold);

                    grid[x + 1][y]++;
                    grid[x - 1][y]++;
                    grid[x][y + 1]++;
                    grid[x][y - 1]++;

                    absDistance = (x + 1) - DIMX / 2 + 1;     //absDistance is the distance between the center and the pixel I'm incrementing

                    if (realRadius < absDistance)    //I refresh the real radius to the farther pixel used
                        realRadius = absDistance;
                }
            }
        }

        // every <frequency> cycles I draw the result, this increase the performances
        if (count == frequency || grid[DIMX / 2][DIMY / 2] < threshold) {
            System.out.println("Real radius: " + realRadius + ", value on the center: " + grid[DIMX / 2][DIMY / 2]);
            Draw();
            count = 1;
            screenCount++;
        } else
            count++;
    }

    private void Draw() {
        mainPanel.repaint();
        //  SaveScreenshot(this);     //used for timelapse
    }

    private boolean isOver() {
        return (grid[DIMX / 2][DIMY / 2] < threshold);
    }

    private void SaveScreenshot(JFrame panel) {

        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        panel.paint(img.createGraphics());
        File dest = new File("C:\\Users\\Lorenzo\\Pictures\\SandPile\\IMG" + String.format("%05d", screenCount) + ".jpeg");
        try {
            dest.createNewFile();
            ImageIO.write(img, "jpeg", dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Color selectColor(int amount) {
        switch (amount) {
            case 1:
                return color1;
            case 2:
                return color2;
            case 3:
                return color3;
            case 0:
                return color0;
            default:
                return color1;
        }

    }

    public static void main(String[] args) {
        SandPile instance = new SandPile("SandPile");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!instance.isOver()) {
            instance.SandSlide();
        }
    }
}
