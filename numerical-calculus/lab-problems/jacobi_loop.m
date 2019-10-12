function x1 = jacobi_loop(A, b, x0)
    n = length(A);
    % x0 = x^(k - 1)
    % x1 = x^(k)
    
    x1 = zeros(n, 1);
    for i = 1:n
       s = 0;
       for j = 1:n
           if i ~= j 
              s = s + A(i, j) * x0(j); 
           end
       end
       x1(i) = (b(i) - s) / A(i, i);
    end
end
