
% pivot: a r.v. W 
%        pdf known
%        P(w(alpha/2) < W < w(1 - alpha/2))

%          mean      | variance     | std deviation
% Pop    | miu(u)    | sigma^2      |    sigma
% Sample | x barat   |   s^2        |    s
%          mean      |   var        |    std

% 1 Pop
% theta:
% u = sigma known e N(0, 1)
% u = sigma unknown e T(n - 1)
% theta = sigma^2    student^2(n-1)

% 2 Pop
% theta = u1 - u2
%  - sigma1 and sigma2 known
%  - sigma1 = sigma2, unknown
%  - sigma1
% theta = sigma1^2/sigma2^2
% sym (N, T)

conf_level = input('conf level = '); % 1 - alpha
alpha = 1 - conf_level;

x1 = [22.4, 21.7, 24.5, 23.4, 21.6, 23.3, 22.4, 21.6, 24.8, 20.0];
x2 = [17.7, 14.8, 19.6, 19.6, 12.1, 14.8, 15.4, 12.6, 14.0, 12.2];

x1_mean = mean(x1);
x2_mean = mean(x2);
x1_n = length(x1);
x2_n = length(x2);
x1_var = var(x1);
x2_var = var(x2);

pooled_var = ((x1_n - 1) * x1_var + (x2_n - 1) * x2_var) / (x1_n + x2_n - 2);
pooled_std = sqrt(pooled_var);

a_t1 = tinv(1 - alpha / 2, x1_n + x2_n - 2);

% a)
a_q_lower = x1_mean - x2_mean - a_t1 * pooled_std * sqrt(1 / x1_n + 1 / x2_n);
a_q_upper = x1_mean - x2_mean + a_t1 * pooled_std * sqrt(1 / x1_n + 1 / x2_n);
fprintf('a) The c.i (miu1 - miu2) where sigma1 = sigma2, is (%f, %f)\n\n', a_q_lower, a_q_upper);

% b)
c = (x1_var / x1_n) / (x1_var /x1_n + x2_var / x2_n);
n = 1 / (c ^ 2 / (x1_n - 1) + (1 - c) ^ 2 / (x2_n - 1));

b_t1 = tinv(1 - alpha/2, n);

b_q_lower = x1_mean - x2_mean - b_t1 * sqrt(x1_var / x1_n + x2_var / x2_n);
b_q_upper = x1_mean - x2_mean + b_t1 * sqrt(x1_var / x1_n + x2_var / x2_n);
fprintf('b) The c.i (miu1 - miu2) where sigma1 != sigma2, is (%f, %f)\n\n', b_q_lower, b_q_upper);

% c)
f1 = finv(1 - alpha / 2, x1_n - 1, x2_n - 1);
f2 = finv(alpha / 2, x1_n - 1, x2_n - 1);

c_q_lower = 1 / f1 * x1_var / x2_var;
c_q_upper = 1 / f2 * x1_var / x2_var;
fprintf('c) The c.i for the ratio of the variances, is (%f, %f)\n\n', c_q_lower, c_q_upper);

fprintf('The c.i for the ratio of the std deviations, is (%f, %f)\n\n', sqrt(c_q_lower), sqrt(c_q_upper));
