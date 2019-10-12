format long;
% 1. is lab7 5 or NOT
%a = 0;
%b = 1;
%fx = @(x) 2 / (1 + x^2);
%epsilon = 10^(-5);
%
%trapezium = trapezium_romberg_epsilon(fx, a, b, epsilon)
%simpson = simpson_romberg_epsilon(fx, a, b, epsilon)


% 2.
%epsilon = 10^-4;
%a = 1;
%b = 3;
%fx = @(x) (100 ./ x .^ 2) .* sin (10 ./ x);
%
%r50_simpson = repeated_simpson(fx, a, b, 50)
%quadrature_simpson = simpson_quadrature(fx, a, b, epsilon)
%
%r100_simpson = repeated_simpson(fx, a, b, 100)
%quadrature_simpson = simpson_quadrature(fx, a, b, epsilon)
%
%% graph
%xfx_values = a:0.01:b;
%yfx_values = fx(xfx_values);
%plot(xfx_values, yfx_values, 'r');

% 3.
%fx = @(x) exp(-x^2);
%a = 1;
%b = 1.5;
%
%n150 = rectangle_repeated(fx, a, b, 150)
%n500 = rectangle_repeated(fx, a, b, 500)

% 4.
a = 0.1;
b = 0.5;
c = 0.01;
d = 0.25;
m = 10;
n = 10;
fxy = @(x, y) exp(y / x);

% 0.178571
simpson_double_integral(fxy, a, b, c, d, m, n)


% optional 1
%a = 0.1;
%b = 0.5;
%c = 0.01;
%d = 0.25;
%m = 10;
%n = 10;
%fxy = @(x, y) exp(y / x);
%
%
%m = 5;
%n = 5;
%
%nodes5 = [0,9062, 0,5385, 0, -0.5385, -0.9062];
%coefficients5 = [0.2369, 0.4786, 0.5689, 0.4786, 0.2369];
%
%gauss_quadrature(fxy, a, b, c, d, m, n, nodes5, coefficients5)
