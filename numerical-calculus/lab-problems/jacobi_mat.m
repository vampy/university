function x = jacobi_mat(A, b, epsilon)
    n = length(A);
    
    L = tril(A, -1);
    U = triu(A, 1);
    D = diag(diag(A));
    D_inv = inv(D);
    
    x0 = zeros(n, 1);
    x1 = D_inv * (-(L + U) * x0 + b);
    k = 1;
    while norm(x1 - x0) > epsilon
        x0 = x1;
        x1 = D_inv * (-(L + U) * x0 + b);
        k = k + 1;
    end

    k
    x = x1;
end
