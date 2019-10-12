%x = 0:0.01:1;
%fx = exp(10 * x .* (x - 1)) .* sin(12 * pi * x);
%plot(x, fx,'--cd');
%title('First Plot of a function');

%a = input('a=');
%b = input('b=');
%t = 0:0.01:10 * pi;
%xt = (a + b) * cos(t) - b * cos((a / b + 1) * t);
%yt = (a + b) * sin(t) - b * sin((a / b + 1) * t);
%
%plot(xt, yt, 'r');
%title('Epicycloid');

% 2.
%x2 = 0:0.001:2*pi;
%ycosx = cos(x2);
%ysinx = sin(x2);
%ycos2x = cos(2 * x2);
%plot(x2, ycosx, x2, ysinx, x2, ycos2x, 'r'); 
%% we can also use subplot(no_of_lines,no_of_columns,no_of_subplot where to draw); 
%legend('cos(x)', 'sin(x)', 'cos(2*x)');

% 3.
%x3even = 0:2:50;
%x3odd = 1:2:50;
%feven = x3even / 2;
%fodd = 3 * x3odd + 1;
%plot(x3even, feven, 'd', x3odd, fodd, 'd');

% 6.
x6 = -2:0.01:2;
y6 = -4:0.01:4;
[x6, y6] = meshgrid(x6,y6);
f6 = exp(-((x6 - 1 / 2) .^ 2 + (y6 - 1 / 2) .^ 2));
mesh(f6);

