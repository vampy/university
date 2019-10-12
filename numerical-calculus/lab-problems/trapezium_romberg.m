function result = trapezium_romberg(f, a, b, k)
    h = b - a;
    
    % base case
    precomputed = [h/2 * (f(a) + f(b))];
    
    % go from 2 to k, aka from 1 to k - 1
    for ik = 2:k
        % access previous value
        first = 1/2 * precomputed(ik - 1);
        % decrease to be correct as the algorithm
        ik = ik - 1;    
        
        powerk = 2 ^ ik;
        second = h / powerk;
        limit = 2 ^ (ik - 1);
        s = 0;
        for j = 1:limit
            s = s + f(a + ((2 * j - 1) / powerk) * h);
        end
        
        result = first + second * s;
        precomputed = [precomputed result];
    end
    
    result = precomputed(k);
end
