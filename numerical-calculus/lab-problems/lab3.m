% 1.
% nodes = [1930, 1940, 1950, 1960, 1970, 1980];
% fx = [123203, 131669, 150697, 179323, 203212, 226505];
% 
% x = input('date = ');
% fprintf('%f \n', lmf(nodes, fx, x));

% 2.
% nodes = [121, 100, 144];
% fx = [11, 10, 12];
% 
% fprintf('%f \n', lmf(nodes, fx, 115));

% 3.
% x = 0:0.05:10;
% fx = (1 + cos(pi * x)) ./ (1 + x);
% xLagr = 0:10/20:10;
% fxLagr = (1 + cos(pi * xLagr)) ./ (1 + xLagr);
% 
% hold on;
% plot(x, fx, 'r');
% 
% ylagr = [];
% for i = 1:length(x)
%     ylagr = [ylagr lmf(xLagr, fxLagr, x(i))];
% end
% plot(x, ylagr);
% legend('fx', 'lmfx');

% 4. this is an exception for the lagrange polynomial.
x = -5:0.01:5;
fx = 1 ./ (1 + x.^2);

xLagr = -5:10/14:5;
fxLagr = 1 ./ (1 + xLagr.^2);

subplot(2, 1, 1);
hold on;
plot(x, fx, 'r');

ylagr = [];
for i = 1:length(x)
    ylagr = [ylagr lmf(xLagr, fxLagr, x(i))];
end
plot(x, ylagr, 'b');
legend('fx', 'lmfx');

% second method
subplot(2, 1, 2);
hold on;
n = 15;
i = 1:n;
a = -5;
b = 5;

xCheb = 1/2 * ((b - a) * cos((2*i - 1) * pi / (2 * n)) + a + b);
fxCheb = 1 ./ (1 + xCheb .^ 2);

plot(x, fx, 'r');

ycheb = [];
for i = 1:length(x)
    ycheb = [ycheb lmf(xCheb, fxCheb, x(i))];
end
ycheb
plot(x, ycheb, 'b');
legend('fx', 'chebfx');



