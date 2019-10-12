import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class File3DObject
{
    private String    filename;
    private Point3D[] points;
    private int       pointsLength;
    private Edge[]    edges;
    private int       edgesLength;
    private int       typeProjection;
    private double    radius;
    private double    alpha;
    public double distanceObserver;
    public double distancePlane;

    public File3DObject(String filename)
    {
        this.filename = filename;
    }

    private String[] getTokensLines(Scanner scan)
    {
        while (scan.hasNextLine())
        {
            String line = scan.nextLine().trim();

            // Ignore empty lines
            if (!line.isEmpty())
            {
                return line.split("\\s+|,\\s+");
            }
        }

        return null;
    }

    public void parseFile() throws File3DObjectException
    {
        Scanner scan;
        try
        {
           scan = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e)
        {
            throw new File3DObjectException(e.getMessage());
        }


        // Read points
        String[] pointsNumber = getTokensLines(scan);
        assert pointsNumber != null && pointsNumber.length >= 1;

        int n = Integer.parseInt(pointsNumber[0]);
        double x, y, z;
        pointsLength = n + 1;
        points = new Point3D[pointsLength];

        for (int i = 1; i < pointsLength; i++)
        {
            String[] tokensPoint = getTokensLines(scan);
            assert tokensPoint != null && tokensPoint.length >= 3;

            x = Double.parseDouble(tokensPoint[0]);
            y = Double.parseDouble(tokensPoint[1]);
            z = Double.parseDouble(tokensPoint[2]);

            points[i] = new Point3D(x, y, z);
        }

        // Read edges
        String[] edgesNumber = getTokensLines(scan);
        assert edgesNumber != null && edgesNumber.length >= 1;

        int m = Integer.parseInt(edgesNumber[0]), left, right;
        edgesLength = m + 1;
        edges = new Edge[edgesLength];

        for (int i = 1; i < edgesLength; i++)
        {
            String[] tokensEdge = getTokensLines(scan);
            assert tokensEdge != null && tokensEdge.length >= 2;

            left = Integer.parseInt(tokensEdge[0]);
            right = Integer.parseInt(tokensEdge[1]);

            edges[i] = new Edge(left, right);
        }

        // Read projection type
        String[] tokensProjection = getTokensLines(scan);
        assert tokensProjection != null && tokensProjection.length >= 3;

        typeProjection = Integer.parseInt(tokensProjection[0]);
        assert typeProjection == 1 || typeProjection == 2;
        double first = Double.parseDouble(tokensProjection[1]);
        double second = Double.parseDouble(tokensProjection[2]);
        if (typeProjection == 1)
        {
            radius = first;
            alpha = second;
        }
        else
        {
            distanceObserver = first;
            distancePlane = second;
        }
    }

    public Point3D[] getPoints()
    {
        return points;
    }

    public Edge[] getEdges()
    {
        return edges;
    }

    public double getRadius()
    {
        return radius;
    }

    public double getAlpha()
    {
        return alpha;
    }

    public int getPointsLength()
    {
        return pointsLength;
    }

    public int getEdgesLength()
    {
        return edgesLength;
    }

    public int getTypeProjection()
    {
        return typeProjection;
    }

    public double getDistanceObserver()
    {
        return distanceObserver;
    }

    public double getDistancePlane()
    {
        return distancePlane;
    }

    public void setAlpha(double alpha)
    {
        this.alpha = alpha;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }
}
