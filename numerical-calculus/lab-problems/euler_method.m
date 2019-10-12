% y' = f(x, y)
% y(x0) = y0
function [x, y] = euler_method(fx, y0, a, b, N)
    h = (b - a) / N;
    
    x = zeros(1, N);
    for i = 1 : N + 1
        x(i) = a + i * h;
    end
        
    y = [y0];
    for i = 1 : N
        value = y(i) + h * fx(x(i), y(i));
        y = [y value];
    end
end
