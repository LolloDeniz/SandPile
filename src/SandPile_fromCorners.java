import javax.swing.*;
import java.awt.*;

@SuppressWarnings("Duplicates")

public class SandPile_fromCorners extends JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();      //fetch screen dimensions
    private static int DIMX = 300, DIMY = 300;
    private static double LOCX = (screenSize.getWidth() - DIMY) / 2, LOCY = (screenSize.getHeight() - DIMX) / 2;

    private Color color0 = Color.BLACK;

    //RAINBOW!!!!
    private static Color color1 = new Color(255, 0, 0);
    private static Color color2 = new Color(255, 127, 0);
    private static Color color3 = new Color(255, 255, 0);
    private static Color color4 = new Color(0, 255, 0);
    private static Color color5 = new Color(0, 0, 255);
    private static Color color6 = new Color(139, 0, 255);
    private static Color color7 = new Color(172, 0, 118);
    private static Color color8 = new Color(251, 249, 247);


    private int[][] grid = new int[DIMX][DIMY];
    private static int startAmount = 5000000;
    private static int threshold = 4;

    private int frequency = 10000;
    private int count = 0;

    private int realRadius = 1;


    public SandPile_fromCorners(String title) {

        super(title);
        this.setSize(DIMX, DIMY);
        this.setLocation((int) LOCX, (int) LOCY);
        this.getContentPane().setBackground(color0);

        //initializing the grid
        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {
                grid[x][y] = 0;
            }
        }

        grid[DIMX - 51][DIMY -51] = startAmount;
        grid[50][50] = startAmount;
        grid[50][DIMY -51] = startAmount;
        grid[DIMX - 51][50] = startAmount;

        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void SandSlide() {
        int[][] newGrid = new int[DIMX][DIMY];
        int absDistance;

        //I just work on the minimum rectangle that contains the draw

        /*int topLeftX = (DIMX / 2 - realRadius);
        int topLeftY = (DIMY / 2 - realRadius);
        int downRightX = (DIMX / 2 + realRadius);
        int downRightY = (DIMY / 2 + realRadius);*/

        //I set to zero all the pixels of the new grid (just the rectangle I work on)
        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {
                newGrid[x][y] = 0;
            }
        }

        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {

                if (grid[x][y] < threshold)
                    newGrid[x][y] += grid[x][y];
                else {
                    newGrid[x][y] += (grid[x][y] - threshold);

                    if (x + 1 < DIMX) newGrid[x + 1][y]++;
                    if (x - 1 >= 0) newGrid[x - 1][y]++;
                    if (y + 1 < DIMY) newGrid[x][y + 1]++;
                    if (y - 1 >= 0) newGrid[x][y - 1]++;

                    /*if (x + 1 < DIMX && y + 1 < DIMY) newGrid[x + 1][y + 1]++;
                    if (x - 1 >= 0 && y + 1 < DIMY) newGrid[x - 1][y + 1]++;
                    if (x + 1 < DIMX && y - 1 >= 0) newGrid[x + 1][y - 1]++;
                    if (x - 1 >= 0 && y - 1 >= 0) newGrid[x - 1][y - 1]++;*/


                    absDistance = (x + 1) - DIMX / 2 + 1;     //absDistance is the distance between the center and the pixel that I'm using

                    if (realRadius < absDistance)    //I refresh the real radius to the farther pixel used
                        realRadius = absDistance;
                }
            }
        }

        grid = newGrid;

        if (count == frequency /*|| grid[DIMX / 2][DIMY / 2] < 4*/) {
            System.out.println("Real radius: " + realRadius + ", value on the center: " + grid[DIMX / 2][DIMY / 2]);
            Draw(realRadius);
            count = 1;
        } else
            count++;
    }


    private void Draw(int realRadius) {
        Graphics g = this.getGraphics();

        /*int topLeftX = (DIMX / 2 - realRadius);
        int topLeftY = (DIMY / 2 - realRadius);
        int downRightX = (DIMX / 2 + realRadius);
        int downRightY = (DIMY / 2 + realRadius);*/

        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {

                if (grid[x][y] > 0) {
                    switch (grid[x][y]) {
                        case 1:
                            g.setColor(color1);
                            break;
                        case 2:
                            g.setColor(color2);
                            break;
                        case 3:
                            g.setColor(color3);
                            break;
                        case 4:
                            g.setColor(color4);
                            break;
                        case 5:
                            g.setColor(color5);
                            break;
                        case 6:
                            g.setColor(color6);
                            break;
                        case 7:
                            g.setColor(color7);
                            break;
                        case 8:
                            g.setColor(color8);
                            break;
                        default:
                            g.setColor(color0);
                            break;
                    }
                    g.drawLine(x, y, x, y);
                }
            }
        }

    }

    public boolean isOver() {
        return (grid[DIMX / 2][DIMY / 2] < threshold);
    }

    public static void main(String[] args) {
        SandPile_fromCorners instance = new SandPile_fromCorners("SandPile_fromCorners");

        while (/*!instance.isOver()*/ true) {
            instance.SandSlide();
        }
    }
}
