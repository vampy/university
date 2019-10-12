function result = lmf(nodes, fx, x)
    numerator = 0;
    denominator = 0;
    for index = 1:length(nodes)
        d_diff = (x - nodes(index));
        
        % Prevent division by zero
        if d_diff != 0
            numerator = numerator + (Aix(nodes, index) * fx(index)) / d_diff;
            denominator = denominator + (Aix(nodes, index)) / d_diff;
        end
    end
    
    result = numerator / denominator;
end