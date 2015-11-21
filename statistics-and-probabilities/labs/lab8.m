x = [20 * ones(1,2), 21, 22*ones(1,3), 23 * ones(1,6), 24*ones(1,5), 25*ones(1,9), 26*ones(1,2), 27*ones(1,2)];
y = [75 * ones(1,3), 76*ones(1,2), 77*ones(1,2),78*ones(1,5),79*ones(1,8),80*ones(1,8), 81, 82];

mx = mean(x); 
my = mean(y);
fprintf('mx = %.2f, my = %.2f\n', mx, my);

vx = var(x, 1); 
vy = var(y, 1);
fprintf('vx = %.2f, vy = %.2f\n', vx, vy);

c = cov(x, y, 1);
cc = corrcoef(x, y);
ccoef = cc(1, 2);

scatter(x, y);
xregr = 20:0.1:27;
yregr = (ccoef * sqrt(vy) / sqrt(vx)) * (xregr - mean(x)) + my;
hold on
plot(xregr, yregr);