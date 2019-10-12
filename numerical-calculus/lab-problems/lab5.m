% 1.
%time_x = [0, 3, 5, 8, 13];
%distance_y = [0, 225, 383, 623, 993];
%time_find = 10.0;
%
%% f'
%speed_y = [75, 77, 80, 74, 72];
%
%distance = hmf(time_x, distance_y, speed_y, time_find);
%fprintf('t = %d, distance = %f, speed = %f\n', time_find, distance, distance/time_find);

% 2. f' = 2 cos (2x)
x = linspace(-5, 5, 1000);
interpolation_points = linspace(-5, 5, 15);

fx = sin(2*x);
fx_interpolation = sin(2 * interpolation_points);
fx_derivated_interpolation = 2 * cos(2 * interpolation_points);

yhmf = [];
for i = 1:length(x)
  yhmf = [yhmf hmf(interpolation_points, fx_interpolation, fx_derivated_interpolation, x(i))];
end

subplot(2, 1, 1);
plot(x, fx, 'r');
legend('fx');

subplot(2, 1, 2);
hold on;
plot(interpolation_points, fx_interpolation, 'b*');
plot(x, yhmf, 'g');

legend('interpolation points', 'hmf');
