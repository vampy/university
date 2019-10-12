function [a, b] = lls_find_ab(x, fx, xfind)
    m = length(x) - 1;
    x_sum = sum(x);
    x_squared_sum = sum(x .^ 2);
    x_sum_squared = x_sum ^ 2;
    
    fx_sum = sum(fx);
    x_product_fx_sum = sum(x .* fx);
  
    denominator = (m + 1) * x_squared_sum - x_sum_squared;
  
    % calculate a
    a = ((m + 1) * x_product_fx_sum - x_sum * fx_sum) / denominator;
    
    % calculate b
    b = (x_squared_sum * fx_sum - x_product_fx_sum * x_sum) / denominator;
end