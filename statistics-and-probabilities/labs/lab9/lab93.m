% s^2 = variance
% quantiles are not symetric 
% chi2 of n - 1

conf_level = input('conf level ='); % 1 - alpha
alpha = 1 - conf_level;

x = [1.48, 1.26, 1.52, 1.56, 1.48, 1.46,...
     1.30, 1.28, 1.43, 1.43, 1.55, 1.57,...
     1.51, 1.53, 1.68, 1.37, 1.47, 1.61,...
     1.49, 1.43, 1.64, 1.51, 1.60, 1.65,...
     1.60, 1.64, 1.51, 1.51, 1.53, 1.74];
 
x_var = var(x);
n = length(x);

chi1 = chi2inv(1 - alpha / 2, n - 1);
chi2 = chi2inv(alpha / 2, n - 1);

q_lower = (n - 1) * x_var / chi1;
q_upper = (n - 1) * x_var / chi2;

fprintf('The c.i for the variance is (%f, %f)\n', q_lower, q_upper);
% variance^2 e ( v1^2, v2^2)
% variance = std e (sqrt(v1^2), sqrt(v2^2)
% can do this only when we have the final values for the variance
fprintf('The c.i for the standard deviation is (%f, %f)\n', sqrt(q_lower), sqrt(q_upper));
