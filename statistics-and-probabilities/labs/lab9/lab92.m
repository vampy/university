% know sample deviation
% tinv(aplha/2, n -1) quantiles
% s is standard deviation
% variance has var
% standard deviation = std or sqrt(var())

conf_level = input('conf level ='); % 1 - alpha
alpha = 1 - conf_level;
x = [99.8 * ones(1, 2), 99.9 * ones(1, 5), 98.0 * ones(1, 3), 100.1 * ones(1, 4), ...
    100.5 * ones(1, 2), 100.0 * ones(1, 2), 100.2 * ones(1, 2)];

x_mean = mean(x);
x_std = std(x);
n = length(x);

% t(alpha/2) = -t(1 - alpha/2)
t1 = tinv(1 - alpha / 2, n - 1);
t2 = tinv(alpha / 2, n - 1);

q_lower = x_mean - (x_std / sqrt(n)) * t1; 
q_upper = x_mean - (x_std / sqrt(n)) * t2;

% the probability that the mean weight will belong to that interval = conf_level
fprintf('The c.i miu of the average weight is (%f, %f)\n', q_lower, q_upper);
