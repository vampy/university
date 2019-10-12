% 1.
% x = 0:0.001:1;
% 
% l1x = x;
% l2x = 3/2 * x.^2 - 1/2;
% l3x = 5/2 * x.^3 - 3/2 * x;
% l4x = 35/8 * x.^4 - 15/4 * x.^2 + 3/8;
% 
% title('Legendre polynomials');
% subplot(2, 2, 1);
% plot(x, l1x);
% 
% subplot(2, 2, 2);
% plot(x, l2x);
% 
% subplot(2,2,3);
% plot(x, l3x);
% 
% subplot(2, 2, 4);
% plot(x, l4x);

% 2.
% a.
% t = -1:0.01:1;
% 
% T1 = cos(acos(t));
% T2 = cos(2 * acos(t));
% T3 = cos(3 * acos(t));
% 
% subplot(3, 1, 1);
% plot(t, T1);
% title('Chebyshev polynomials');
% 
% subplot(3, 1, 2);
% plot(t, T2);
% 
% subplot(3, 1, 3);
% plot(t, T3);

% b.
% x = -1:0.01:1;
% n = 10; 
% t0x = 1;
% t1x = x;
% hold;
% for i = 1:n
%   current = 2 * x .* t1x - t0x;
%   
%   plot(x, current);
%   
%   t0x = t1x;
%   t1x = current;
% end

% 3.
% x = -1:0.01:3;
% x0 = 0;
% n = 6;
% 
% hold;
% plot(x, exp(x), 'm');
% 
% for k = 0:n
%     pnx = 0;
%     for i = 0:k
%         pnx = pnx + (((x - x0) .^ i) / factorial(i)) * exp(x0);
%     end
%     %pnx = sum((((x - x0) .^ k) / factorial(k)) * exp(x0), 1);
%     plot(x, pnx, 'b');
%     
% end
% legend('fx = e^x', '1', '2', '3', '4', '5', '6');

% 4.
% h = 0.25;
% a = 1;
% result = zeros(5);
% ai = a:h:a+4*h;
% % for i = 0:4
% %    a + i * h 
% % end
% 
% % replace the first column
% %result(:, 1) = transpose(ai);
% 
% result(:, 1) = [0;2;6;14;17];
% for j = 2:5
%    for i = 1:6-j
%         result(i, j) = result(i + 1, j - 1) - result(i, j - 1);
%    end
% end
% result


% 5.
x = [2, 4, 6, 8];
%result = zeros(length(x));
%result(:, 1) = [4; 8; 14; 16];
%
%% first divided differencce
%for j = 2:length(x)
%   for i = 1:length(x) - j + 1
%       % k is the column j - 1
%       result(i, j) = (result(i + 1, j - 1) - result(i, j - 1)) / (x(i + j - 1) - x(i));
%   end
%end
result = divided_difference([4; 8; 14; 16],  x, length(x))

