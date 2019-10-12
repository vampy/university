% m is how much we must multiply

% 1. p : n - lines p to n
% p - column
% q - is the pos found

% x = b - sum

A = [1 1 1 1; 2 3 1 5; -1 1 -5 3; 3 1 7 -2];
b = [10; 31; -2; 18];

n = length(b);
gauss(n, A, b)

% 2.
A = [6 2 1 -1; 2 4 1 0; 1 1 4 -1; -1 0 -1 3];
b = [8; 7; 5; 1];
n = length(b);
doolittle(n, A, b)
%A \ b
%x_test = [1; 1; 1; 1];
%A * x_test