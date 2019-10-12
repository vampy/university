% y' = f(x, y)
% y(x0) = y0
function [x, y] = runge_kutta_method(fx, y0, a, b, N)
    h = (b - a) / N;
    
    x = zeros(1, N + 1);
    for i = 1 : N + 1
        x(i) = a + i * h;
    end
        
    y = [y0];
    for i = 1 : N
        k1 = h * fx(x(i), y(i));
        k2 = h * fx(x(i) + h / 2, y(i) + 1/2 * k1);
        k3 = h * fx(x(i) + h / 2, y(i) + 1/2 * k1);
        k4 = h * fx(x(i + 1), y(i) + k3);
        
        value = y(i) + 1/6 * (k1 + 2 * k2 + 2 * k3 + k4);
        y = [y value];
    end
end
