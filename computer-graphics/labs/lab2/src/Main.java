import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Main extends JPanel implements ActionListener
{
    // Points should always be, left -> top, right -> bottom
    private Point2D viewportLeft, viewportRight, windowLeft, windowRight;

    private final int FPS              = 60;
    private final int RADIUS           = 5;
    private final int TIMER_REFRESH_MS = 1000 / FPS;
    File3DObject file;

    private Timer      timer;
    private JPanel     form_panel;
    private JButton    loadCube;
    private JButton    resetFromFile;
    private JButton    loadPyarmid;
    private JButton    firstButton;
    private JTextField secondText;
    private JTextField firstText;
    private JButton    setButton;
    private JLabel     firstLabel;
    private JLabel secondLabel;

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

        file = new File3DObject("cube.txt");
        file.parseFile();
        setWindowFromObjectLimits();

        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("Lab 2");
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

        frame.setVisible(true);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null); // Center frame.
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Init hooks
        loadCube.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                self.file = new File3DObject("cube.txt");
                self.file.parseFile();
                self.setWindowFromObjectLimits();
            }
        });
        loadPyarmid.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                self.file = new File3DObject("pyramid.txt");
                self.file.parseFile();
                self.setWindowFromObjectLimits();
            }
        });
        resetFromFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                self.file.parseFile();
                self.setWindowFromObjectLimits();
            }
        });

        firstButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                double first = Double.parseDouble(self.firstText.getText());
                self.file.setRadius(first);
                self.setWindowFromObjectLimits();
            }
        });

        setButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                double second = Double.parseDouble(self.secondText.getText());
                self.file.setAlpha(second);
                self.setWindowFromObjectLimits();
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

    public void setWindowFromObjectLimits()
    {
        Point3D[] points = file.getPoints();
        int pointsLength = file.getPointsLength();
        double radius = file.getRadius(), alpha = file.getAlpha(),
               distanceObserver = file.getDistanceObserver(), distancePlane = file.getDistancePlane();

        // Find Drawing limits
        windowLeft = points[1].toParallelProjection2D(radius, alpha);
        windowRight =  new Point2D(windowLeft.x, windowLeft.y);

        for (int i = 2; i < pointsLength; i++)
        {
            Point2D point2D;
            if (file.getTypeProjection() == 1)
            {
                point2D = points[i].toParallelProjection2D(radius, alpha);
            }
            else
            {
                point2D = points[i].toPerspectiveProjection2D(distanceObserver, distancePlane);
            }

            // X
            if (point2D.x < windowLeft.x)
            {
                // can go to the left more
                windowLeft.x = point2D.x;
            }
            else if (point2D.x > windowRight.x)
            {
                // can go to the right more
                windowRight.x = point2D.x;
            }

            // Y
            if (point2D.y < windowRight.y)
            {
                // can go lower
                windowRight.y = point2D.y;
            }
            else if (point2D.y > windowLeft.y)
            {
                // can go upper
                windowLeft.y = point2D.y;
            }
        }
//        windowLeft.x = Math.ceil(windowLeft.x);
//        windowLeft.y = Math.ceil(windowLeft.y);
//        windowRight.x = Math.ceil(windowRight.x);
//        windowRight.y = Math.ceil(windowRight.y);

        System.out.println("Window coordinates: ");
        System.out.println(windowLeft);
        System.out.println(windowRight);
        System.out.println();
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
     *
     * @param x the window X coordinate
     * @return the viewport X coordinate
     */
    public int getUFromX(double x)
    {
        return (int) ((x - windowLeft.x) * (viewportRight.x - viewportLeft.x) / (windowRight.x - windowLeft.x) + viewportLeft.x);
    }

    /**
     * Get the viewport coordinates from the window (real coordinates)
     *
     * @param y the window Y coordinate
     * @return the viewport Y coordinate
     */
    public int getVFromY(double y)
    {
        return (int) ((y - windowLeft.y) * (viewportRight.y - viewportLeft.y) / (windowRight.y - windowLeft.y) + viewportLeft.y);
    }

    /**
     * Get the real coordinates from the viewport
     *
     * @param u the viewport X coordinate
     * @return the window X coordinate
     */
    public double getXFromU(double u)
    {
        return (u - viewportLeft.x) * (windowRight.x - windowLeft.x) / (viewportRight.x - viewportLeft.x) + windowLeft.x;
    }

    /**
     * Get the real coordinates from the viewport
     *
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
        g2d.fillRect(this.getUFromX(x), this.getVFromY(y), 1, 1);
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
    }

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        setDefaultFont(g2d);
        g2d.drawString("Viewport", (int) viewportLeft.x - 10, (int) viewportLeft.y - 10);

        g2d.setPaint(Color.blue);

//        // top line
//        drawLineRaw(g2d, viewportLeft.x, viewportLeft.y, viewportRight.x, viewportLeft.y);
//
//        // right line
//        drawLineRaw(g2d, viewportRight.x, viewportLeft.y, viewportRight.x, viewportRight.y);
//
//        // bottom line
//        drawLineRaw(g2d, viewportRight.x, viewportRight.y, viewportLeft.x, viewportRight.y);
//
//        // top line
//        drawLineRaw(g2d, viewportLeft.x, viewportRight.y, viewportLeft.x, viewportLeft.y);

        Point2D centerPoint = new Point2D(0, 0);
        g2d.setPaint(Color.black);
//        drawAxes(g2d, centerPoint, 2f);
//        drawTextAroundAxes(g2d, "X", "Y", centerPoint, 1);

        g2d.setPaint(Color.green);

        Point3D[] points = file.getPoints();
        Edge[] edges = file.getEdges();
        int edgesLength = file.getEdgesLength();
        double radius = file.getRadius(), alpha = file.getAlpha(),
            distanceObserver = file.getDistanceObserver(), distancePlane = file.getDistancePlane();

        for (int i = 1; i < edgesLength; i++)
        {
            // Defined in 3D real space
            Point3D from3D = points[edges[i].left];
            Point3D to3D = points[edges[i].right];

            if (file.getTypeProjection() == 1)
            {
                drawLineViewport(g2d,
                    from3D.toParallelProjection2D(radius, alpha),
                    to3D.toParallelProjection2D(radius, alpha)
                );
            }
            else
            {
                drawLineViewport(g2d,
                    from3D.toPerspectiveProjection2D(distanceObserver, distancePlane),
                    to3D.toPerspectiveProjection2D(distanceObserver, distancePlane)
                );
            }
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
