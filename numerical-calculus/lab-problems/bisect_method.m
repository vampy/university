function x = bisect_method(fx, a, b, eps, N)
    an = a;
    bn = b;
    
    an_next = 0;
    bn_next = 0;
    for n = 0 : N
       c = (an + bn) / 2;
       
       if fx(an) * fx(c) <= 0
          an_next = an;
          bn_next = c;
       else
           an_next = c;
           bn_next = bn;
       end
       
       % eps
       if abs(an - bn) < eps
          x = (an + bn) / 2;
          n
          break
       end
       
       an = an_next;
       bn = bn_next;
    end
    
end