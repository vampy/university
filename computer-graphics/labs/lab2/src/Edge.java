public class Edge
{
    // Only keep information about point indices
    public int left, right;

    public Edge(int left, int right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString()
    {
        return "Edge{" +
            "left=" + left +
            ", right=" + right +
            '}';
    }
}
