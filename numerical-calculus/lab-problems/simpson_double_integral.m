function result = simpson_double_integral(f, a, b, c, d, m, n)
    h = (b - a) / (2 * n);
    j1 = 0;
    j2 = 0;
    j3 = 0;
    
    for i = 0:2 * n
       x = a + i * h;
       hx = (d - c) / (2 * m);
       
       k1 = f(x, c) + f(x, d);
       k2 = 0;
       k3 = 0;
       
       for j = 1:2 * m - 1
          y = c + j * hx;
          z = f(x, y);
          if mod(j, 2) == 0 
             k2 = k2 + z; 
          else
              k3 = k3 + z;
          end
       end
       
       l = (k1 + 2 * k2 + 4 * k3) * hx / 3;
       
       if i == 0 || i == 2 * n
            j1 = j1 + l;
       else
           if mod(i, 2) == 0
               j2 = j2 + l;
           else
               j3 = j3 + l;
           end
       end
    end
    
    result = (j1 + 2 * j2 + 4 * j3) * h / 3;
end
