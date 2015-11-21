% 2.
% H0: miu0 = 99.4 (miu0 < 99.4)
% H1: miu0 > 99.4
% we do a ttest because we do not know sigma

alpha = input('sign. level = ');
x = [99.8 * ones(1, 2), 99.9 * ones(1, 5), 98.0 * ones(1, 3), 100.1 * ones(1, 4), ...
    100.5 * ones(1, 2), 100.0 * ones(1, 2), 100.2 * ones(1, 2)];
miu0 = 99.4; % average mean

[h, p, ci, stats] = ttest(x, miu0, alpha, 1); % tail is right
% h - same
% p - same
% ci - same
% stats: - tstat: T0
%        - df aka (n - 1)
%        - s (std)
% we extract it with stats.total

if h == 0
    fprintf('Do not reject H0. They will buy the energy bars.\n');
else
    fprintf('Reject H0. They will not buy the energy bars.\n');
end

t = tinv(1 - alpha, stats.df); % stats.df == n - 1
fprintf(' P value = %f\n T0(test statisitc) = %f\n RR(rejection region) is (%f, %f)\n', p, stats.tstat, t, inf);
fprintf('\n\n');
