n = input('a = ');
i = 1:n % index
step = (b-a)/n
limits = a:step:b
left_lim = limits(i) % 1:n
right_lim = limits(i+1) % 2:n
mid_points = (right_lim + left_lim)/2