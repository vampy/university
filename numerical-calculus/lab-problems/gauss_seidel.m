function x = gauss_seidel(A, b, epsilon)
    n = length(A);
    
    x0 = zeros(n, 1);
    x1 = x0;
    x1 = gauss_seidel_loop(A, b, x0, x1);
    k = 1;
    
    while norm(x1 - x0) > epsilon
        x0 = x1;
        x1 = gauss_seidel_loop(A, b, x0, x1);
        k = k + 1;
    end

    k
    x = x1;
end
