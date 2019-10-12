function x = doolittle(n, A, b)
    % ek = line in I
    % ek has 1 on the column k    
    % aik ^ (k-1) = U(i, k)
    I = eye(n);
    
    ll = A;
    
    final_k = n - 1;
    tk = zeros(1, final_k);
    
    % set bellow main diagonal
    % go over columns
    for k = 1 : n - 1
        
        % go over lines
        for i = k + 1 : n
           ll(i, k) = A(i, k) / A(k, k);
        end
    end
    
    % build tk
    for i = final_k + 1 : n
        tk = [tk ll(i, final_k)];
    end

    tk = transpose(tk);
%    ll

    ek = zeros(1, n);   
    ek(final_k) = 1;
    
    Mk = I - tk * ek;
    U = Mk * A;
    L = inv(Mk);
    
    % L*U = A
%    L * U
    
    % L * U * x = b

    % decompose and replace with variable z so that is easier to compute
    % L * z = b
    z = L \ b;
    
    % U * x = z
    x = U \ z;
end
