function result = simpson_quadrature(f, a, b,epsilon)
    h = b - a;
    median = (a + b) / 2;
    
    I1 = repeated_simpson(f, a, b, 1);
    I2 = repeated_simpson(f, a, median, 1) + repeated_simpson(f, median, b, 1);

    if abs(I1 - I2) < 15 * epsilon
        result = I2;
    else
        result = simpson_quadrature(f, a, median, epsilon / 2) + ...
            simpson_quadrature(f, median, b, epsilon / 2);
    end
end
