function result = divided_difference(x, fx)
    len = length(x);
    result = zeros(len);
    
    % fx is the first column
    result(:, 1) = fx;

    % first divided differencce
    for j = 2:len
       for i = 1:len - j + 1
           % k is the column j - 1
           result(i, j) = (result(i + 1, j - 1) - result(i, j - 1)) / (x(i + j - 1) - x(i));
       end
    end
end
