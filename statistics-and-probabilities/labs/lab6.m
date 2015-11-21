N = input('sample size=');


distr = input('1. Normal\n 2. Exp');

switch distr
    case 1
        % Normal distr
        mu = input('mu=');
        sigma = input('sigma=');
        sample = sort(normrnd(mu, sigma, 1, N));
        [m, v] = normstat(mu, sigma);
        pop = m - 3 * sqrt(v):0.01:m + 3 * sqrt(v); % simulate population(3sigma rule)
        spdf = normpdf(sample, mu, sigma);
        ppdf = normpdf(pop, mu, sigma);
        scdf = normcdf(sample, mu, sigma);
        pcdf = normcdf(pop, mu, sigma);
    case 2
        mu = input('mu=');
        sample = sort(exprnd(mu, 1, N));
        [m, v] = expstat(mu);
        pop = m - 3 * sqrt(v):0.01:m + 3 * sqrt(v); % simulate population(3sigma rule)

        spdf = exppdf(sample, mu);
        ppdf = exppdf(pop, mu);
        scdf = expcdf(sample, mu);
        pcdf = expcdf(pop, mu);
end


subplot(2, 1, 1);

plot(sample, spdf, '*', pop, ppdf, 'R');
legend('sample pdf', 'pop pdf', 1);

subplot(2, 1, 2);

plot(sample, scdf, '*', pop,pcdf, 'R');
legend('sample cdf', 'pop cdf', 1);