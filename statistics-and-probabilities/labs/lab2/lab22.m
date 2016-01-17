n = input('n = ');
a = input('a = ');
b = input('b = ');
i = 1:n; % index
step = (b - a) / n;
limits = a:step:b;
left_lim = limits(i); % 1:n
right_lim = limits(i + 1); % 2:n
mid_points = (right_lim + left_lim) / 2;

% matlabul merge pe coloane
result = [i; left_lim; right_lim; mid_points];

fprintf(' Nr | Subinterval \t\t | Midpoint|\n');
fprintf('%3d | (%3.5f, %3.5f) | %3.5f |\n', result);