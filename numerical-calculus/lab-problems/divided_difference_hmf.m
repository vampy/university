function result = divided_difference_hmf(x, fx, fx_derivated)
    len = length(x);
    result = zeros(len);
    
    % fx is the first column
    result(:, 1) = fx;

    % first divided differencce
    for j = 2:len
       for i = 1:len - j + 1
           % k is the column j - 1
           
           % put derivates of fx
           if mod(i, 2) == 1 && j == 2
              result(i, j) = fx_derivated(i);
           else % normal difference
              result(i, j) = (result(i + 1, j - 1) - result(i, j - 1)) / (x(i + j - 1) - x(i));
           end
       end
    end
end
