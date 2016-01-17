x = 0:0.01:3;
f1 = (x .^ 2) ./ 10;
f2 = x .* sin(x);
f3 = cos(x);
% plot(x, f1, 'k:')
% hold on
% plot(x, f2, 'b-.')
% hold on
% plot(x, f3, 'm--')
% title('Three functions')
% legend('G1', 'G2', 'G3', -1)

% position
subplot(3, 1, 1);
plot(x, f1);
subplot(3, 1, 2);
plot(x, f2);
subplot(3, 1, 3);
plot(x, f3);

% input('a=4')