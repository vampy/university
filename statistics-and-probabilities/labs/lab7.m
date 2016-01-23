
N = input('sample size = ');
a = input('a = ');
b = input('b (>a) = ');

x = unifrnd(a, b, 1, N); % sample
n = 1 + 10 / 3 * log10(N);
i = 1:n;

% lim min(x) : (max - min)/n: max(x)
%hist(x, n); % histogram
[freq, mark] = hist(x, n);

min_x = min(x);
max_x = max(x);
step = (max_x - min_x) / n;
limits = min_x : step : max_x;

left_lim = limits(i);
right_lim = limits(i + 1);
relative_freq = freq ./ N;

result = [i; left_lim; right_lim; mark; freq; relative_freq];

%          i    [ci, ci + 1]    xi       fi         nfi
fprintf(' Nr | Class \t\t\t| Mark \t | Freq \t | Rel. Freq |\n');
fprintf('%3d | [%3.4f, %3.4f]  | %3.4f | %3.4f \t | %3.4f \t |\n', result);

clf;
hist(x, n); % histogram
hold on;
plot(mark, freq, 'r');

% median does not mean middle 
mean_arh = mean(x);
mean_geo = geomean(x);
mean_har = harmmean(x);
med = median(x);

mode_index = find(freq == max(freq));
mode_result = [mode_index; limits(mode_index); limits(mode_index + 1); freq(mode_index); relative_freq(mode_index)];

fprintf('arhitmetic = %3.4f, geometric = %3.4f, harmmean = %3.4f, median = %3.4f\n', mean_arh, mean_geo, mean_har, med);
fprintf('The modes: \n');
fprintf('%3d | [%3.4f, %3.4f]  | %3.4f | %3.4f \t | %3.4f \t |\n\n', mode_result);

Q = prctile(x, [25, 50, 75]);
fprintf('\nThe quartiles: Q1 = %f, Q2 = %f, Q3 = %f\n', Q(1), Q(2), Q(3));
