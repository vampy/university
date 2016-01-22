% 1 Pop
% theta = miu
% theta = sigma^2

% 2 Pop
% theta = miu1 - miu2
% theta = sigma1^2/sigma^2

% a)
% H0: sigma1 = sigma2   sigma1/sigma2 = 1    sigma1^2/sigma2^2 = 1
% H1: sigma1 != sigma2  sigma1/sigma2 != 1   sigma1^2/sigma2^2 != 1
% [H, P, CI, stats] = use vartest2
% h = 0 => do not reject H0
% h = 1 => reject H1
% P - value
% CI - confidence interval is bilateral, we do not care in this exercise
% stats - fstat TS0 F(n1 -1, n2 - 1), F = fisher distribution
%       - df1 n1 -1
%       - df2 n2 -a

% todo: print observed value, reject region, P value
% quantiles are finv

alpha = input('sign. level = ');
x1 = [22.4, 21.7, 24.5, 23.4, 21.6, 23.3, 22.4, 21.6, 24.8, 20.0];
x2 = [17.7, 14.8, 19.6, 19.6, 12.1, 14.8, 15.4, 12.6, 14.0, 12.2];

[h, p, ci, stats] = vartest2(x1, x2, alpha, 0); % 0 both ends test

fprintf('a) Test for variances\n');
if h == 0
    fprintf('Do not reject H0. Variances of the populations are equal.\n');
else
    fprintf('Reject H0. Variances of the populations are not equal.\n');
end


f1 = finv(alpha / 2, stats.df1, stats.df2);
f2 = finv(1 - alpha / 2, stats.df1, stats.df2);
fprintf(' P value = %f\n TS(test statistic) = %f\n RR(rejection region) is (%f, %f) U (%f, %f) \n', p, stats.fstat, -inf, f1, f2, inf);
fprintf('\n\n');
% alpha < P and TS !e RR

% b)
% use right tailed test
% H0: miu1 = miu2   miu1 - miu2 = 0
% H1: miu1 > miu2   miu1 - miu2 > 0

% ttest2
% - vartype param: 'equal', 'unequal'
% - stats: 
%        - tstat TS0
%        - df    n1 + n2 - 2
%        - sd    (pooled variance)


[h, p, ci, stats] = ttest2(x1, x2, alpha, 1, 'equal');

fprintf('b) Tests for means\n');
if h == 0
    fprintf('Do not reject H0. Gas mileage is not higher.\n');
else
    fprintf('Reject H0. Gas mileage is higher when using premium gasoline.\n');
end

t1 = tinv(1 - alpha, stats.df);
fprintf(' P value = %e\n TS(test statistic) = %f\n RR(rejection region) is (%f, %f)\n', p, stats.tstat, t1, inf);
fprintf('\n\n');
% alpha >= P and TS e RR


