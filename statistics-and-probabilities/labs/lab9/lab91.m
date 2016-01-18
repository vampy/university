% ------ 1 ----------
% 1 - alpha = confidence level
% P(Q e (Ql, Qu)) = 1 - alpha
% z = norminv
conf_level = input('conf level ='); % 1 - alpha
alpha = 1 - conf_level;
sigma = 5;
x = [7, 7, 4, 5, 9, 9,...
    4, 12, 8, 1, 8, 7,...
    3, 13, 2, 1, 17, 7,...
    12, 5, 6, 2, 1, 13,...
    14, 10, 2, 4, 9, 11,...
    3, 5, 12, 6, 10, 7];

x_mean = mean(x);
n = length(x);

% not every quantile is the opposite of the other
% z(1/2) = -z(1 - 1/2)
% quantiles inv
z1 = norminv(1 - alpha / 2, 0, 1);
z2 = norminv(alpha / 2, 0, 1);
q_lower = x_mean - (sigma / sqrt(n)) * z1; 
q_upper = x_mean - (sigma / sqrt(n)) * z2;

fprintf('The c.i miu is (%f, %f)\n', q_lower, q_upper);

% in stats there are 3 levels
% 95%, 99%, 99.9%
