function result = gauss_quadrature(f, a, b, c, d, m, n, nodes, coeff)
    % WARMNIG,  with m = 5 and n = 5, only works with nodes5 and coeff5, 555555555 WARNING
    % TODO create matrix for nodes and coeff
    h1 = (b - a) / 2;
    h2 = (b + a) / 2;
    result = 0;
    for i = 1:m
       jx = 0;
       x = h1 * nodes(i) + h2;
       k1 = (d - c) / 2;
       k2 = (d + c) / 2;
       for j = 1:n
          y = k1 * nodes(j) + k2;
          jx = jx + coeff(j) * f(x, y);
          jx
       end
        result = result + coeff(i) * k1 * jx;
    end

    result = h1 * result;
end
