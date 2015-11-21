% lab11.
% We always have 2 hypt
% null: H0 (always the simple one): theta = theta0
% alt: H1 (always the opposite of H0)
%      - theta < theta0  
%      - theta > theta0
%      - theta != theta0
%
% Given alpha e (0, 1) sign. level
% alpha = P(type I error) = P(reject H0 | H0) = P(TS0 e RR | H0) (determine
% the rejection) !!!?? = P(TS e RR | theta = theta0)
% - test statistic. TS
% - observed value of the TS, TS0 = TS (theta = theta0)
% - rejection region. RR
% - P - value (the probability that the TS has 
%   1. TS0 e RR - yes -> reject H0
%               - no  -> do not reject H0("accept")
%   2. alpha >= P -> reject H0
%      alpha < P  -> do not reject H0

% 1. a)
% H0: u = 9 (u >= 9)
% H1: u < 9

alpha = input('sign. level = ');
x = [7, 7, 4, 5, 9, 9,...
    4, 12, 8, 1, 8, 7,...
    3, 13, 2, 1, 17, 7,...
    12, 5, 6, 2, 1, 13,...
    14, 10, 2, 4, 9, 11,...
    3, 5, 12, 6, 10, 7];

sigma = 5;
miu0 = 9; % average mean
% TS = Z e N(0, 1), use ztest
% tail: -1 = left, 0 two, 1 right
[h, p, ci, zval] = ztest(x, miu0, sigma, alpha, -1);

% h - 0 -> do not reject H0
%   - 1 -> reject H0
% p = P
% ci = confindence interval
% zval - Z0(TS0)

fprintf('\nFor the mean.\n');
if h == 0
    fprintf('Do not reject H0. The standard is met.\n');
else
    fprintf('Reject H0. The standard is not met, needs to be replaced.\n');
end

z = norminv(alpha, 0, 1); % Calculate RR = (-infinity, z(alpha));
fprintf(' P value = %f\n Z0(test statistic) = %f\n RR(rejection region) is (%f, %f)\n', p, zval, -inf, z);
fprintf('\n\n');



% 1. b
% H0: sigma = 5 i.e. sigma^2 = 25
% H1: sigma != 5 i.e. sigma^2 != 25

[h, p, ci, stats] = vartest(x, sigma^2, alpha, 0); % both
% stats - chisqstat (chi^2
%       - df (n - 1)

q0 = chi2inv(alpha/2, stats.df);
q1 = chi2inv(1 - alpha/2, stats.df);

fprintf('For the variance.\n');
if h == 0
    fprintf('Do not reject H0. Assumption is correct.\n');
else
    fprintf('Reject H0. Assumption is incorect.\n');
end

fprintf(' P value = %f\n test statistic = %f\n RR(rejection region) is (%f, %f) U (%f, %f)\n', p, stats.chisqstat, -inf, q0, q1, inf);
fprintf('\n\n');
