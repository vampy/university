format long;
% A = [3, -1, 0, 0, 0, 0; 
%     -1, 3, -1, 0, 0, 0;
%     0, -1, 3, -1, 0, 0;
%     0, 0, -1, 3, -1, 0;
%     0, 0, 0, -1, 3, -1;
%     0, 0, 0, 0, -1, 3];

% 1.
fprintf('\n---------------\n1.\n-------------------\n');
n = 6;
A =  diag(ones(n, 1) * 3, 0) + diag(ones(n - 1, 1) * -1, 1) + diag(ones(n - 1, 1) * -1, -1);

b = [2; 1; 1; 1; 1; 2];

epsilon = 10^-3;

jac = jacobi(A, b, epsilon)
gaus = gauss_seidel(A, b, epsilon)

w = 1.25;
rel = relaxation(A, b, epsilon, w)

% 2.
fprintf('\n---------------\n2.\n-------------------\n');
A = [3, 1, 1; 
    -2, 4, 0; 
    -1, 2, -6];
b = [12; 2; -5];
epsilon = 10^-5;

% tril(A, -1)
% triu(A, 1)
% diag(A)
% diag(diag(A))

jac = jacobi_mat(A, b, epsilon)
gaus = gauss_seidel_mat(A, b, epsilon)
w = 0.9;
rel = relaxation_mat(A, b, epsilon, w)

fprintf('\n---------------\n\n\n\n\n\n-------------------\n');