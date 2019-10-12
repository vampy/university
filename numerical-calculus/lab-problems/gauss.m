function x = gauss(n, A, b)
    x = zeros(1, n);
    for p = 1:n - 1
        [value, q] = max(abs(A(p:n, p)));
        % k is p
        
        % because of the subrows
        q = q + p - 1;
        
        if A(q, p) == 0
           fprintf('EXIT');
           return 
        end
        
        % interchange
        if p ~= q
            line_p = A(p, :);
            line_q = A(q, :);
           
            % swap A
            temp = line_q;
            line_q = line_p;
            line_p = temp;
            
            A(p, :) = line_p;
            A(q, :) = line_q;
            
            % swap b
            temp = b(q);
            b(q) = b(p);
            b(p) = temp;
        end
        
        % all lines below p
        for i = p + 1 : n
            % k is p
            mik = A(i, p) / A(p, p);
            
            % from p to n
            A(i, p:n) = A(i, p:n) - A(p, p:n) * mik; 
            b(i) = b(i) - b(p) * mik;
        end
    end
    
    if A(n, n) == 0
       fprintf('EXIT 2');
       return 
    end
    
    % calculate x
    for i = n : -1 : 1
        demonimator = A(i, i);
        
        % right hand side - left hand side, see Course 8 page 15
        numerator = b(i) - sum(A(i, i+1:n) .* x(i+1:n));

        x(i) = numerator / demonimator;
    end
end
