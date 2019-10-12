% 1.
%x = [1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00];
%fx = [13, 15, 20, 14, 15, 13, 10];
%xfind = 8.00;
%
%[a, b] = lls_find_ab(x, fx)
%
%fprintf('time %f = %f temperature \n', xfind, lls_eval(a, b, xfind));
%
%xlls = 0 : 0.07 : 12;
%ylls = [];
%for i = 1:length(xlls)
%    ylls = [ylls lls_eval(a, b, xlls(i))];
%end
%
%hold on;
%plot(x, fx, 'b*');
%plot(xlls, ylls, 'r');
%legend('points', 'lls');

% 2.
%%temperature
%x = [0, 10, 20, 30, 40, 60, 80, 100];
%
%% pressure
%fx = [0.0061, 0.0123, 0.0234, 0.0424, 0.0738, 0.1992, 0.4736 1.0133];
%
%% a)
%xfind = 45;
%p45 = 0.095848;
%
%pthird_fit = polyfit(x, fx, 3);
%pfourth_fit = polyfit(x, fx, 4);
%pthird = polyval(pthird_fit, xfind);
%pfourth = polyval(pfourth_fit, xfind);
%
%fprintf('P(%f) with degree 3 = %f with error %f\n', xfind, pthird, pthird - p45);
%fprintf('P(%f) with degree 4 = %f with error %f\n', xfind, pfourth, pfourth - p45);
%
%% b)
%xpoints = linspace(0, 100, 1000);
%ypolynomial = polyval(pfourth_fit, xpoints);
%
%ylagr = [];
%for i = 1:length(xpoints)
%    ylagr = [ylagr lmf(x, fx, xpoints(i))];
%end
%
%hold on;
%plot(x, fx, 'b*');
%plot(xpoints, ypolynomial, 'r');
%plot(xpoints, ylagr);
%legend('points', 'polynomial', 'lmf');

%% 3.
%x = -3 : 0.4 : 3;
%y = sin(x);
%
%xpoints = linspace(-3, 3, 1000);
%ypoints = polyval(polyfit(x, y, 4), xpoints);
%
%hold on;
%plot(x, y, 'b*');
%plot(xpoints, ypoints, 'r');
%legend('points', 'polynomial');

% 4.
% Must provide input with mouse
axis([0, 3, 0, 5]);
[x, y] = ginput(10);

xpoints = linspace(0, 3, 100);
ypolyomial = polyval(polyfit(x, y, 9), xpoints);

hold on;
plot(x, y, 'b*');

plot(xpoints, ypolyomial, 'r');
