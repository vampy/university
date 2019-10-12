function x1 = relaxation_loop(A, b, x0, x1, w)
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
       
       x1(i) = (w / A(i, i)) * (b(i) - s1 - s2) + (1 - w) * x0(i);
    end
end
