function result = hmf(x, fx, fx_derivated, xfind)
    % double elements
    x = double_elm(x);
    fx = double_elm(fx);
    fx_derivated = double_elm(fx_derivated);
    
    % calculate the differece table
    differences_table = divided_difference_hmf(x, fx, fx_derivated);
    x0divided_row = differences_table(1, 2:end);
    
    % compute hmf, the same as nmf
    result = fx(1);
    for i = 1:length(x0divided_row)
        product = 1;
        for j = 1:i
            product = product * (xfind - x(j));
        end
        result = result + product * x0divided_row(i);
    end
end
