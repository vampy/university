% Ax = b
% A is a matrix of (n x n)
% x column vector (n x 1)
% b column vector (n x 1)
% cond(A) = || A || * || A^-1 ||
% if cond(A) > 1000 the solution is ill formed
% || A || - norm/length of A

% 1.
%% a)
%A = [400, -201; -800, 401];
%b = [200; -200];
%
%%inv(A) * b
%x = A \ b;
%x
% 
% 
%% b)
%A = [401, -201; -800, 401];
%b = [200; -200];
%
%%inv(A) * b
%x = A \ b;
%x
%
%% c.
%if cond(A) <= 1000
%    fprintf('Cond is good form\n'); 
%else
%    fprintf('Cond is ill formed\n');
%end

% 2.
%A = [10, 7, 8, 7; 7, 5, 6, 5; 8, 6, 10, 9; 7, 5, 9, 10];
%b = [32; 23; 33; 31];
%
%% a.
%if cond(A) <= 1000
%    fprintf('Cond is good form\n'); 
%else
%    fprintf('Cond is ill formed\n');
%end
%
%x = A \ b;
%x
%
%
%% b.
%b1 = [32.1; 22.9; 33.1; 30.9];
%x1 = A \ b1;
%x1
%
%input_relative_error = norm(b - b1)/ norm(b);
%output_relative_error = norm(x - x1) / norm(x);
%
%input_relative_error
%output_relative_error
%
%fprintf('output_relative_error / input_relative_error = %f\n', output_relative_error / input_relative_error);
%
%% c.
%fprintf('\n');
%A2 = [10, 7, 8.1, 7.2; 7.08, 5.04, 6, 5; 8, 5.98, 9.89, 9; 6.99, 4.99, 9, 9.98];
%x2 = A2 \ b;
%x2
%
%input_relative_error = norm(A - A2)/ norm(A);
%output_relative_error = norm(x - x2) / norm(x);
%
%input_relative_error
%output_relative_error
%fprintf('output_relative_error / input_relative_error = %f\n', output_relative_error / input_relative_error);


% 3.
%n_sizes = 10:1:15;
%
%for n = n_sizes
%    Hn = zeros(n);
% 
%     for i = 1:n
%         for j = 1:n
%             Hn(i, j) = 1 / (i + j - 1);
%         end
%     end
%    
%    fprintf('H%d, cond(H%d) = %f\n', n, n, cond(Hn));
%end

% 4.
n_sizes = 10:1:15;

% NOTE: octave has this by default
format long;

% a.
fprintf('\n\n4.a) \n');
for n = n_sizes
    Vtk = zeros(n);
    k = 1:1:n;
    
    tk = zeros(1, n);
    for i = k
       tk(i) = 1 / i; 
    end
 
    for j = k
       Vtk(:, j) = tk .^ (j - 1); 
    end
    
    fprintf('cond(Vt%d) = %f\n', n, cond(Vtk));
end

% b.
fprintf('\n\n b) \n');
for n = n_sizes
    Vtk = zeros(n);
    k = 1:1:n;
    
    tk = zeros(1, n);
    for i = k
       tk(i) = -1 + (2 / n) * i; 
    end

    for j = k
       Vtk(:, j) = tk .^ (j - 1); 
    end
    
    fprintf('cond(vander_matlab(Vt%d)) = %f\n', n, cond(vander(tk)));
    fprintf('cond(Vt%d) = %f\n', n, cond(Vtk));
end

