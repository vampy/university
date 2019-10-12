function result = simpson_romberg(f, a, b, k)
    h = b - a;
    
    % base case
    if k == 0
      result = h/6 * (f(a) + 4 * f(a + h/2) + f(b));
      return
    end
    
    % general case
    result = 1/3 * (4 * trapezium_romberg(f, a, b, k + 1) - trapezium_romberg(f, a, b, k));
end
