function result = rectangle_repeated(f, a, b, n)
    x1 =  a + (b - a) / (2 * n);
    
    s = f(x1);
    for i = 2:n
        xi = x1 + (i - 1) * ((b - a) / n);
        s = s + f(xi);
    end
   
    result = ((b - a) / n) * s;
end
