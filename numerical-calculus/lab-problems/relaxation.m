function x = relaxation(A, b, epsilon, w)
    n = length(A);
    
    x0 = zeros(n, 1);
    x1 = x0;
    x1 = relaxation_loop(A, b, x0, x1, w);
    k = 1;
    
    while norm(x1 - x0) > epsilon
        x0 = x1;
        x1 = relaxation_loop(A, b, x0, x1, w);
        k = k + 1;
    end

    k
    x = x1;
end
