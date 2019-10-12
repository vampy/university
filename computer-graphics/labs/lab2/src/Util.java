public class Util
{
    // Polar coordinates (r, alpha)
    static public double getParallelProjectionX(double x, double z, double radius, double alpha)
    {
        return x + radius * z * Math.cos(alpha);
    }

    static public double getParallelProjectionY(double y, double z, double radius, double alpha)
    {
        return y + radius * z * Math.sin(alpha);
    }

    // See: http://www.cs.ubbcluj.ro/~per/Grafica/Gr_Calc_C2.pdf
    static public double getPerspectiveProjectionX(double x, double z, double distanceObserver, double distancePlane)
    {
        return x * (distanceObserver - distancePlane) / (distanceObserver - z);
    }

    static public double getPerspectiveProjectionY(double y, double z, double distanceObserver, double distancePlane)
    {
        return y * (distanceObserver - distancePlane) / (distanceObserver - z);
    }
}
