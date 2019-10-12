function result = nmf(x, fx, xfind)
    % calculate the difference table
    differences_table = divided_difference(x, fx);
    x0divided_row = differences_table(1, 2:end);
    
    result = fx(1);
    % length(nodes) - 1 or length(x0divided_row)
    for i = 1:length(x0divided_row)
        product = 1;
        for j = 1:i
            product = product * (xfind - x(j));
        end
        result = result + product * x0divided_row(i);
    end
end
