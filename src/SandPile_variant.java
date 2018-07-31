import javax.swing.*;
import java.awt.*;

@SuppressWarnings("Duplicates")

public class SandPile_variant extends JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();      //fetch screen dimensions
    private static int DIMX = 600, DIMY = 600;
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
    private static int threshold = 8;

    private int frequency = 50;
    private int count = 0;

    private int realRadius = 1;


    public SandPile_variant(String title) {

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
        grid[DIMX / 2 ][DIMY / 2 ] = startAmount;


        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void SandSlide() {
        int[][] newGrid = new int[DIMX][DIMY];
        int absDistance;

        //I just work on the minimum rectangle that contains the draw

        int topLeftX = (DIMX / 2 - realRadius);
        int topLeftY = (DIMY / 2 - realRadius);
        int downRightX = (DIMX / 2 + realRadius);
        int downRightY = (DIMY / 2 + realRadius);

        //I set to zero all the pixels of the new grid (just the rectangle I work on)
        for (int x = topLeftX - 1; x <= downRightX + 1; x++) {
            for (int y = topLeftY - 1; y <= downRightY + 1; y++) {
                newGrid[x][y] = 0;
            }
        }

        for (int x = topLeftX; x <= downRightX; x++) {
            for (int y = topLeftY; y <= downRightY; y++) {

                if (grid[x][y] < threshold)
                    newGrid[x][y] += grid[x][y];
                else {
                    newGrid[x][y] += (grid[x][y] - threshold);

                    newGrid[x + 1][y]++;
                    newGrid[x - 1][y]++;
                    newGrid[x][y + 1]++;
                    newGrid[x][y - 1]++;

                    newGrid[x + 1][y + 1]++;
                    newGrid[x - 1][y + 1]++;
                    newGrid[x + 1][y - 1]++;
                    newGrid[x - 1][y - 1]++;


                    absDistance = (x + 1) - DIMX / 2 + 1;     //absDistance is the distance between the center and the pixel that I'm using

                    if (realRadius < absDistance)    //I refresh the real radius to the farther pixel used
                        realRadius = absDistance;
                }
            }
        }

        grid = newGrid;

        if (count == frequency || grid[DIMX / 2][DIMY / 2] < 4) {
            System.out.println("Real radius: " + realRadius + ", value on the center: " + grid[DIMX / 2][DIMY / 2]);
            Draw(realRadius);
            count = 1;
        } else
            count++;
    }


    private void Draw(int realRadius) {
        Graphics g = this.getGraphics();

        int topLeftX = (DIMX / 2 - realRadius);
        int topLeftY = (DIMY / 2 - realRadius);
        int downRightX = (DIMX / 2 + realRadius);
        int downRightY = (DIMY / 2 + realRadius);

        for (int x = topLeftX; x <= downRightX; x++) {
            for (int y = topLeftY; y <= downRightY; y++) {

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
        SandPile_variant instance = new SandPile_variant("SandPile_variant");

        while (/*!instance.isOver()*/ true) {
            instance.SandSlide();
        }
    }
}
