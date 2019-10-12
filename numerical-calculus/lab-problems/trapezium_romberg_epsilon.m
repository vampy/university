function result = trapezium_romberg_epsilon(f, a, b, epsilon)
    n = 3; % TODO
    
    first = trapezium_romberg(f, a, b, n);
    second = trapezium_romberg(f, a, b, n - 1);
    while abs(first - second) > epsilon
        n = n + 1;
        second = first;
        first = trapezium_romberg(f, a, b, n);
    end
%    abs(first - second)
    result = first;
end
