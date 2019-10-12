function x = relaxation_mat(A, b, epsilon, w)
    n = length(A);
    
    L = tril(A, -1);
    U = triu(A, 1);
    D = diag(diag(A));
    DwL_inv = inv(D + w * L);
    
    x0 = zeros(n, 1);
    x1 = DwL_inv * (((1 - w) * D - w * U) * x0 + w * b);
    k = 1;
    while norm(x1 - x0) > epsilon
        x0 = x1;
        x1 = DwL_inv * (((1 - w) * D - w * U) * x0 + w * b);
        k = k + 1;
    end

    k
    x = x1;
end
