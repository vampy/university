import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Main extends JPanel implements ActionListener
{
    // Points should always be, left -> top, right -> bottom
    private Point2D viewportLeft, viewportRight, windowLeft, windowRight;

    private final int    FPS              = 60;
    private final int    RADIUS           = 5;
    private final int    TIMER_REFRESH_MS = 1000 / FPS;
    private final double POINTS_PRECISION = 0.01;

    // Add more points, 100/0.001/100/10
    private final int NR_DRAW_POINTS = (int)(TIMER_REFRESH_MS / POINTS_PRECISION / TIMER_REFRESH_MS / FPS);

    // Animation members
    private ArrayList<Point2D> animationPointsAstroid = new ArrayList<>();
    private int                animationAstroidIndex  = 0;
    private boolean            isAnimation            = false;
    private boolean            isAnimationDone        = true;
    private boolean            isAnimationPaused      = true;
    private boolean isDrawReversed = false;

    private Timer   timer;
    private JPanel  form_panel;
    private JButton animateAstroidButton;
    private JButton drawAstroidButton;
    private JButton reverseDrawDirectionButton;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                final Main surface = new Main();
            }
        });
    }

    // P(x, y) - point in the window (real space)
    // M(u, v) - point in the viewport (on what swing actually draws on)
    public Main()
    {
        double x = 30, y = 30;
        // Corresponds to our physical window
        viewportLeft = new Point2D(x, y);
        viewportRight = new Point2D(1024, 576); // 1024x576 is aspect ration 16:9

        // Window corresponds with our real plane which will be mapped into our physical window
        // See http://www.cs.ubbcluj.ro/~per/Grafica/Gr_Calc_C1.pdf (page 11)
        windowLeft = new Point2D(-16, 9);
        windowRight = new Point2D(16, -9);

        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("Lab 1");
        frame.setContentPane(form_panel); // Set parent
        frame.add(this);

        final Main self = this;
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                self.stopTimer();
            }
        });
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                double x, y;

                x = self.getXFromU(e.getX());
                y = self.getYFromV(e.getY());

                System.out.println(String.format("Point = %f, %f", x, y));
                System.out.println();
                super.mouseClicked(e);
            }
        });

        frame.setVisible(true);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); // Center frame.
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Init hooks
        form_panel.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                System.out.println("Resized");
            }
        });

        animateAstroidButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Will toggle to false if this is true now
                if (self.isAnimationPaused)
                {
                    self.animateAstroidButton.setText("Pause Animation");
                }
                else
                {
                    self.animateAstroidButton.setText("Play Animation");
                }

                // toggle Pause state
                self.isAnimationPaused = !self.isAnimationPaused;
                self.isAnimation = true;
                self.isAnimationDone = false;
            }
        });
        drawAstroidButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                self.resetAnimation();
            }
        });
        reverseDrawDirectionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                self.resetAnimation();
                self.isDrawReversed = !self.isDrawReversed;
                self.animationPointsAstroid.clear();
            }
        });

        this.startTimer();
    }

    public void startTimer()
    {
        timer = new Timer(TIMER_REFRESH_MS, this);
        timer.start();
    }

    public void stopTimer()
    {
        if (timer != null)
        {
            timer.stop();
            timer = null;
        }
    }

    public void resetAnimation()
    {
        isAnimation = false;
        isAnimationDone = true;
        isAnimationPaused = true;
        animationAstroidIndex = 0;
        animateAstroidButton.setText("Play Animation");
    }

    public void setDefaultFont(Graphics2D g2d)
    {
        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setFont(new Font("Serif", Font.PLAIN, 14));
    }

    /**
     * Get the viewport coordinate from the window (real coordinates)
     * @param x the window X coordinate
     * @return the viewport X coordinate
     */
    public int getUFromX(double x)
    {
        return (int) ((x - windowLeft.x) * (viewportRight.x - viewportLeft.x) / (windowRight.x - windowLeft.x) + viewportLeft.x);
    }

    /**
     * Get the viewport coordinates from the window (real coordinates)
     * @param y the window Y coordinate
     * @return the viewport Y coordinate
     */
    public int getVFromY(double y)
    {
        return (int) ((y - windowLeft.y) * (viewportRight.y - viewportLeft.y) / (windowRight.y - windowLeft.y) + viewportLeft.y);
    }

    /**
     * Get the real coordinates from the viewport
     * @param u the viewport X coordinate
     * @return the window X coordinate
     */
    public double getXFromU(double u)
    {
        return (u - viewportLeft.x) * (windowRight.x - windowLeft.x) / (viewportRight.x - viewportLeft.x) + windowLeft.x;
    }

    /**
     * Get the real coordinates from the viewport
     * @param v the viewport Y coordinate
     * @return the window Y coordinate
     */
    public double getYFromV(double v)
    {
        return (v - viewportLeft.y) * (windowRight.y - windowLeft.y) / (viewportRight.y - viewportLeft.y) + windowLeft.y;
    }

    public void drawLineRaw(Graphics2D g2d, double x1, double y1, double x2, double y2)
    {
        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public void drawLineViewport(Graphics2D g2d, double x1, double y1, double x2, double y2)
    {
        drawLineRaw(g2d, this.getUFromX(x1), this.getVFromY(y1), this.getUFromX(x2), this.getVFromY(y2));
    }

    public void drawLineViewport(Graphics2D g2d, Point2D A, Point2D B)
    {
        drawLineViewport(g2d, A.x, A.y, B.x, B.y);
    }

    public void drawPointViewport(Graphics2D g2d, double x, double y)
    {
//        g2d.fillRect(this.getUFromX(x), this.getVFromY(y), 1, 1);
        drawLineViewport(g2d, x, y, x, y);
    }

    public void drawPointViewport(Graphics2D g2d, Point2D A)
    {
        drawPointViewport(g2d, A.x, A.y);
    }

    public void drawAxes(Graphics2D g2d, double centerX, double centerY, float dash_density)
    {
        g2d = (Graphics2D) g2d.create();
        float[] dash = {dash_density, 0f, dash_density};
        BasicStroke bs1 = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        g2d.setStroke(bs1);

        // X axis, Y is variable
        drawLineViewport(g2d, windowLeft.x, centerY, windowRight.x, centerY);

        // Y axis, X is variable
        drawLineViewport(g2d, centerX, windowLeft.y, centerX, windowRight.y);

        g2d.dispose();
    }

    public void drawAxes(Graphics2D g2d, Point2D center, float dash_density)
    {
        drawAxes(g2d, center.x, center.y, dash_density);
    }

    public void drawTextAroundAxes(Graphics2D g2d, String xStr, String yStr, double centerX, double centerY, double offset)
    {
        // Draw around X, down
        g2d.drawString(xStr, this.getUFromX(windowRight.x - offset / 1.5), this.getVFromY(centerY - offset / 1.5));

        // Draw around Y, left
        g2d.drawString(yStr, this.getUFromX(centerX - offset / 1.5), this.getVFromY(windowLeft.y - offset / 1.5));
    }

    public void drawTextAroundAxes(Graphics2D g2d, String xStr, String yStr, Point2D center, double offset)
    {
        drawTextAroundAxes(g2d, xStr, yStr, center.x, center.y, offset);
    }

    public void drawTextAroundAxes(Graphics2D g2d, Point2D center, double offset, Point2D draw)
    {
        NumberFormat formatter = new DecimalFormat("#0.00");
        drawTextAroundAxes(g2d, formatter.format(draw.x), formatter.format(draw.y), center, offset);
//        drawTextAroundAxes(g2d, String.valueOf((int)center.x), String.valueOf((int)center.y), center, offset);
//        drawTextAroundAxes(g2d, String.valueOf(getUFromX(center.x)), String.valueOf(getVFromY(center.y)), center, offset);
//        drawTextAroundAxes(g2d, String.valueOf(getXFromU(center.x)), String.valueOf(getYFromV(center.y)), center, offset);
    }

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        setDefaultFont(g2d);
        g2d.drawString("Viewport", (int)viewportLeft.x - 10, (int)viewportLeft.y - 10);

        g2d.setPaint(Color.blue);

        // top line
        drawLineRaw(g2d, viewportLeft.x, viewportLeft.y, viewportRight.x, viewportLeft.y);

        // right line
        drawLineRaw(g2d, viewportRight.x, viewportLeft.y, viewportRight.x, viewportRight.y);

        // bottom line
        drawLineRaw(g2d, viewportRight.x, viewportRight.y, viewportLeft.x, viewportRight.y);

        // top line
        drawLineRaw(g2d, viewportLeft.x, viewportRight.y, viewportLeft.x, viewportLeft.y);

        // draw axes from the circle center point
        double x, y, cost, sint;

        Point2D centerPoint = new Point2D(0, 0);
        g2d.setPaint(Color.black);
        drawAxes(g2d, centerPoint, 2f);
        drawTextAroundAxes(g2d, "X", "Y", centerPoint, 1);

        g2d.setPaint(Color.green);
        drawPointViewport(g2d, 0, 0);

        // draw circle and astroid
        boolean initAnimationPoints = animationPointsAstroid.isEmpty();
        double t, twoPI = 2 * Math.PI;
        if (isDrawReversed)
        {
            t = twoPI;
        }
        else
        {
            t = 0;
        }

        while (true)
        {
            if (isDrawReversed)
            {
                if (t < 0)
                {
                    break;
                }
            }
            else
            {
                if (t > twoPI)
                {
                    break;
                }
            }

            // draw circle
            cost = Math.cos(t);
            sint = Math.sin(t);
            x = RADIUS * cost;
            y = RADIUS * sint;
            g2d.setPaint(Color.blue);
            drawPointViewport(g2d, x, y);

            // https://en.wikipedia.org/wiki/Astroid
            x = RADIUS * Math.pow(cost, 3);
            y = RADIUS * Math.pow(sint, 3);
            g2d.setPaint(Color.red);
            if (isAnimation && !isAnimationDone)
            {
                // Add animation points
                if (initAnimationPoints)
                {
                    animationPointsAstroid.add(new Point2D(x, y));
                }
            }
            else // draw them all
            {
                drawPointViewport(g2d, x, y);
            }

            if (isDrawReversed)
            {
                t -= POINTS_PRECISION;
            }
            else
            {
                t += POINTS_PRECISION;
            }
        }

        // animate
        if (isAnimation && !isAnimationDone)
        {
            // Draw points up to that the last point
            g2d.setPaint(Color.red);
            for (int i = 0; i < animationAstroidIndex; i++)
            {
                drawPointViewport(g2d, animationPointsAstroid.get(i));
            }

            if (!isAnimationPaused)
            {
                // Add more points
                int endLimit = animationPointsAstroid.size() - 1;
                for (int i = 0; i < NR_DRAW_POINTS; i++, animationAstroidIndex++)
                {
                    drawPointViewport(g2d, animationPointsAstroid.get(i));

                    // completed an animation
                    if (animationAstroidIndex == endLimit)
                    {
                        this.resetAnimation();
                        break;
                    }
                }
            }

            // Draw axes around the last drawn point
            Point2D lastPoint = animationPointsAstroid.get(animationAstroidIndex);
            g2d.setPaint(Color.green);
            this.drawAxes(g2d, lastPoint, 4);
            this.drawTextAroundAxes(g2d, lastPoint, 1.8, lastPoint);
        }

        g2d.dispose();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void repaint()
    {
        super.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
}
