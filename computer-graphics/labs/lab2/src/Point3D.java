public class Point3D
{
    public double x, y, z;

    public Point3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D scaleCoordinates(double modifier)
    {
        return new Point3D(x + modifier, y + modifier, z);
    }

    // Polar coordinates (r, alpha)
    public Point2D toParallelProjection2D(double radius, double alpha)
    {
        return new Point2D(
            Util.getParallelProjectionX(x, z, radius, alpha),
            Util.getParallelProjectionY(y, z, radius, alpha)
        );
    }

    public Point2D toPerspectiveProjection2D(double distanceObserver, double distancePlane)
    {
        return new Point2D(
            Util.getPerspectiveProjectionX(x, z, distanceObserver, distancePlane),
            Util.getPerspectiveProjectionY(y, z, distanceObserver, distancePlane)
        );
    }

    @Override
    public String toString()
    {
        return "Point3D{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            '}';
    }
}
