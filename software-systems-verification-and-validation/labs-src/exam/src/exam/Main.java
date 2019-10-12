package exam;


import java.util.ArrayList;

public class Main
{

    public static void main(String[] args)
    {
        System.out.println("Exam running");
    }

    public void replaceAllFromIntervalWithValue(ArrayList l, int a, int b, int e) throws ValueException
    {
        // 1
        if (a > b)
        {
            // 2
            throw new ValueException("invalid  data  value");
        }

        // 3, bug here
        if ((l.size() == 1) && ((int) l.get(0) < a || (int) l.get(0) > b))
        {
            // 4
            l.set(0, e);
        }
        else
        {
            // 5, bug here
            int i = 0;
            // 6
            while (i < l.size())
            {
                // 7
                if (((int) l.get(i) < a) || ((int) l.get(i) > b))
                {
                    // 8
                    l.set(i, e);
                }
                // 9
                i++;
            }
        }
    }

    public void r1eplaceAllFromIntervalWithValue(ArrayList l, int a, int b, int e) throws ValueException
    {
        if (a > b)
            throw new ValueException("invalid data value");

        if ((l.size() == 1) && ((int) l.get(0) < a && (int) l.get(0) > b))
            l.set(0, e);
        else
        {
            int i = 2;
            while (i < l.size())
            {
                if (((int) l.get(i) < a) || ((int) l.get(i) > b))
                    l.set(i, e);
                i++;
            }
        }
    }
}
