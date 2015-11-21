% use binomial distribution
% P(X < 2) -> binocdf(1, 3, 1/2)
% P(X <= 2) -> binocdf(2, 3, 1/2)
n = input('n(nr of trials) = ');
p = input('p(prob of success) = ');
x = 0:n;
cx = 0:0.001:n;

pdist = binopdf(x, n, p);
cdist = binocdf(cx, n, p);

subplot(2, 1, 1)
plot(x, dist, 'r*');
subplot(2, 1, 2)
plot(cx, cdist, 'b-');
