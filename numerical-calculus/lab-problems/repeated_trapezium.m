function result = repeated_trapezium(f, a, b, n)
    s = 0;
    
    xk = linspace(a, b, n + 1);
    
    for k = 2:length(xk) - 1
        s = s + f(xk(k));
    end
  
    result = ((b - a) / (2 * n)) * ((f(a) + f(b) + 2 * s));
end
