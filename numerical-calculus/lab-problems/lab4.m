% 1.
%nodes = [1, 2, 3, 4, 5];
%fx = [22, 23, 25, 30, 28];
%
%nmf(nodes, fx, 2.5)
%
%hold on;
%plot(nodes, fx, 'r*');
%
%xnmfx = linspace(1, 5, 100);
%
%ynmf = [];
%for i = 1:length(xnmfx)
%  ynmf = [ynmf nmf(nodes, fx, xnmfx(i))];
%end
%
%plot(xnmfx, ynmf);
%legend('fx', 'nmfx');

%2.
%x = linspace(0, 6, 100);
%interpolation_points = linspace(0, 6, 13);
%fx = exp(sin(x));
%fx_interpolation = exp(sin(interpolation_points));
%
%ymnf = [];
%for i = 1:length(x)
%    ymnf = [ymnf nmf(interpolation_points, fx_interpolation, x(i))];
%end
%
%hold on;
%plot(interpolation_points, fx_interpolation, 'b*');
%plot(x, fx, 'r');
%plot(x, ymnf, 'g');
%
%legend('interpolation points', 'fx', 'nmf');

% 3.
x = [81, 100, 121, 144, 169];
xfind  = 115;
[S, I] = sort(abs(xfind - x));
% I is the index of the sorted ones in X
x = x(I);

fx = [9, 10, 11, 12, 13];
fx = fx(I);
m = length(x);
aitken_table = zeros(m);
aitken_table(:, 1) = fx;
epsilon = 10^-3;

% f00 |
% f10 | f11
% f20 | f21
% ... |
% fm0 | fm1 ...mm
%
% eg. | f22 - f11 | < epsilon
% unitil | fii - fi-1,i-1 | < epsilon
% fii will be our aproximation
result = 0;
for i = 1:m
   for j = 1:i-1
       determinant = aitken_table(j, j) * (x(i) - xfind) - (x(j) - xfind) * aitken_table(i, j);
       aitken_table(i, j + 1) = ( 1 / (x(i) - x(j)) ) * determinant;
   end
   
   if i > 1
       if  abs(aitken_table(i, i) - aitken_table(i - 1, i - 1)) < epsilon
           result = aitken_table(i, i);
           fprintf('sqrt(%d) = %f\n', xfind, result);
           break
       end
   end

end

if result == 0
  fprintf('give more points\n') 
end
result
aitken_table
