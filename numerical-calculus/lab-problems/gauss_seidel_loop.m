function x1 = gauss_seidel_loop(A, b, x0, x1)
    n = length(A);
    % x0 = x^(k - 1)
    % x1 = x^(k)
    
    for i = 1:n
       s1 = 0;
       for j = 1:i - 1
            s1 = s1 + A(i, j) * x1(j); 
       end
       
       s2 = 0;
       for j = i + 1:n
          s2 = s2 + A(i, j) * x0(j); 
       end
       
       x1(i) = (b(i) - s1 - s2) / A(i, i);
    end
end
