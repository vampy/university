function x = newton_method(fx, fx_der, x0, eps, N)
    
    % x0 is xn-1
    % x1 is xn
    x1 = 0;
    for i = 0 : N
       x1 = x0 - fx(x0)/fx_der(x0);

       % eps
       if abs(x1 - x0) < eps
          x = x1;
          i
          break
       end
       
       x0 = x1;
    end
end