% 1.
%%a and b
%
%fx = @(x) 2 ./ (1 + x .^ 2);
%trapezium_x = @(f, a, b) ((b - a) / 2) * (f(a) + f(b));
%simpson_x = @(f, a, b) ((b - a) / 6) * (f(a) + 4*f((a + b)/2) + f(b));
%
%b = 1;
%a = 0;
%
%trapezium_value = trapezium_x(fx, a, b)
%xfx_values = 0:0.01:1;
%yfx_values = fx(xfx_values);
%
%hold on;
%plot(xfx_values, yfx_values, 'r');
%
%x = [0, 0, 1, 1];
%y = [0, fx(0), fx(1), 0];
%plot(x, y, 'b');
%legend('fx', 'trapez');
% 
% % c
%simpson_value = simpson_x(fx, a, b)

% 2.
%fx = @(x, y) log(x + 2*y);
%double_trapezium_x = @(f, a, b, c, d) (((b - a)*(d - c)) / 16) * ...
% (f(a, c) + f(a, d) + f(b, c) + f(b, d) + ...
% 2 * f((a + b)/2, c) + 2 * f((a + b)/2, d) + ...
% 2 * f(a, (c + d)/2) + 2 * f(b, (c + d)/2) + ...
% 4 * f((a + b)/2, (c + d)/2));
%
%a = 1.4;
%b = 2;
%c = 1;
%d = 1.5;
%
%double_trapezium_x(fx, a, b, c, d)

% 3.
%r = 110;
%x = 75;
%a = 0;
%b = 2 * pi;
%fphi = @(phi) sqrt(1 - (x/r)^2 * sin(phi));
%
%
%tr_value = repeated_trapezium(fphi, a, b, 10)
%(60 * r) / (r ^ 2 - x ^ 2) * tr_value

% 4.
%a = 0;
%b = pi;
%
%fx = @(x) 1 / (4 + sin(20 * x));
%
%n_10 = repeated_simpson(fx, a, b, 10)
%n_30 = repeated_simpson(fx, a, b, 30)

%5.
a = 0;
b = 1;
fx = @(x) 2 / (1 + x^2);
epsilon = 10^(-5);

trapezium_romberg_epsilon(fx, a, b, epsilon)

