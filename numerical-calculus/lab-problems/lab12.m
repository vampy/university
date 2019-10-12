format long;
% (x0, y0) <=> y - y0 = f'(x0)(x - x0)
% y = 0
% x1 = x0 - f(x0) / f'(x0)

% 1.
fprintf('\n1.\n');
fx = @(x) cos(x) - x;
fx_der = @(x) -sin(x) - 1;
N = 100;
x0 = pi / 4;
eps = 10^-4;

newton_method(fx, fx_der, x0, eps, N)

% 2.
fprintf('\n2.\n');
x0 = 1;
x1 = 2;
fx = @(x) x^3 - x^2 - 1;
eps = 10^-4;
N = 100;

secant_method(fx, x0, x1, eps, N)

% 3.
fprintf('\n3.\n');
fx = @(x) (x - 2)^2 - log(x); 
eps = 10^-4;
N = 100;
a = 1;
b = 2;

% secant_method(fx, a, b, eps, N)
bisect_method(fx, a, b, eps, N)

% 4.
fprintf('\n4.\n');
% y' = f(x, y)
% y(x0) = y0
a = 0;
b = 1;
N = 10;

y0 = -1;

fx_approx = @(x, y) 2 * x - y;

[x, y] = euler_method(fx_approx, y0, a, b, N);

hold on;
plot(x, y, '*');

% 
x_exact = linspace(a, b, 100);
fx_exact = @(x) exp(-x) + 2*x - 2;
y_exact = arrayfun(fx_exact, x_exact);

plot(x_exact, y_exact, 'r');

[x, y] = runge_kutta_method(fx_approx, y0, a, b, N);
plot(x, y, 'g+');

legend('euler points', 'exact solution', 'runge kutta');

