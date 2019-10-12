function x = secant_method(fx, xn_prev, xn_cur, eps, N)
    % xn_prev = x(n - 1)
    % xn_cur = x(n)
    % x_next = x(n + 1)
    
    for k = 1 : N
       xn_next = xn_cur - fx(xn_cur) * ((xn_cur - xn_prev) / (fx(xn_cur) - fx(xn_prev)));

       % eps
       if abs(xn_next - xn_cur) <= eps
          x = xn_next;
          k
          break
       end
       
       xn_prev = xn_cur;
       xn_cur = xn_next;
    end
    
end