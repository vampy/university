function result = simpson_romberg_epsilon(f, a, b, epsilon)
    n = 3; % TODO
    first = simpson_romberg(f, a, b, n);
    second = simpson_romberg(f, a, b, n - 1);
    while abs(first - second) > epsilon
        n = n + 1;
        second = first;
        first = simpson_romberg(f, a, b, n);
    end
    
    result = first;
end
