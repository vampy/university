function result = repeated_simpson(f, a, b, n)
    xk = linspace(a, b, n + 1);
    
    s1 = 0;
    for k = 2:length(xk)
        s1 = s1 + f((xk(k - 1) + xk(k)) / 2);
    end
    
    s2 = 0;
    for k = 2:length(xk) - 1
        s2 = s2 + f(xk(k));
    end
  
    result = ((b - a) / (6 * n)) * ((f(a) + f(b) + 4 * s1 + 2 * s2));
end
