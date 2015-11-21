
fprint('1. Normal\n 2. Student\n 3. Chi\n 4. Fisher\n');
law = input('law = ');

x0 = input('x0 = ');
x1 = input('x1 = ');
x2 = input('x2 = ');
alpha = input('alpha(0, 1) = ');
beta = input('beta(0, 1) = ');
switch law
    case 1
        % Normal distribution
        mu = input('mu(real) = ');
        sigma = input('sigma(> 0) = ');

        % a)
        pa = normcdf(x0, mu, sigma);

        % b) the opposite of a)
        pb = 1 - pa;

        % c) F(b) - F(a)
        pc_x1 = normcdf(x1, mu, sigma);
        pc_x2 = normcdf(x2, mu, sigma);
        pc = pc_x2 - pc_x1;

        % d) not pc_x1 + not pc_x2
        pd = 1 - pc;

        % e) F(xa) = a => xa = F^(-1)(a) (inverse of the cdf)
        Xe = norminv(alpha, mu, sigma);

        % f) 1 - P(X < Xb) = B, P(X < Xb)  = 1 - B, F(Xb) = 1 - B, Xb = F^(-1)(1 - B) 
        Xf = norminv(1 - beta, mu, sigma);

        fprintf('Probabilit(Quantila) at: \n a) %3.5f\n b) %3.5f\n c) %3.5f\n d) %3.5f\n', pa, pb, pc, pd);
        fprintf('X at: e) %3.5f\n f) %3.5f\n', Xe, Xf);
    case 2
        % Student distribution
        n = input('n(integer) = ');

        % a)
        pa = tcdf(x0, n);

        % b) the opposite of a)
        pb = 1 - pa;

        % c) F(b) - F(a)
        pc_x1 = tcdf(x1, n);
        pc_x2 = tcdf(x2, n);
        pc = pc_x2 - pc_x1;

        % d) not pc_x1 + not pc_x2
        pd = 1 - pc;

        % e) F(xa) = a => xa = F^(-1)(a) (inverse of the cdf)
        Xe = tinv(alpha, n);

        % f) 1 - P(X < Xb) = B, P(X < Xb)  = 1 - B, F(Xb) = 1 - B, Xb = F^(-1)(1 - B) 
        Xf = tinv(1 - beta, n);
    case 3
        % Chi distribution
        n = input('n(integer) = ');

        % a)
        pa = fcdf(x0, n);

        % b) the opposite of a)
        pb = 1 - pa;

        % c) F(b) - F(a)
        pc_x1 = chi2cdf(x1, n);
        pc_x2 = chi2cdf(x2, n);
        pc = pc_x2 - pc_x1;

        % d) not pc_x1 + not pc_x2
        pd = 1 - pc;

        % e) F(xa) = a => xa = F^(-1)(a) (inverse of the cdf)
        Xe = chi2inv(alpha, n);

        % f) 1 - P(X < Xb) = B, P(X < Xb)  = 1 - B, F(Xb) = 1 - B, Xb = F^(-1)(1 - B) 
        Xf = chi2inv(1 - beta, n);
    case 4
        % Student distribution
        n = input('n(integer) = ');
        m = input('m(integer) = ');

        % a)
        pa = fcdf(x0, n, m);

        % b) the opposite of a)
        pb = 1 - pa;

        % c) F(b) - F(a)
        pc_x1 = fcdf(x1, n, m);
        pc_x2 = fcdf(x2, n, m);
        pc = pc_x2 - pc_x1;

        % d) not pc_x1 + not pc_x2
        pd = 1 - pc;

        % e) F(xa) = a => xa = F^(-1)(a) (inverse of the cdf)
        Xe = fcdf(alpha, n, m);

        % f) 1 - P(X < Xb) = B, P(X < Xb)  = 1 - B, F(Xb) = 1 - B, Xb = F^(-1)(1 - B) 
        Xf = fcdf(1 - beta, n, m);
    otherwise
        error('wrong law')
end

fprintf('Quantila at: \n a) %3.5f\n b) %3.5f\n c) %3.5f\n d) %3.5f\n', pa, pb, pc, pd);
fprintf('X at: e) %3.5f\n f) %3.5f\n', Xe, Xf);
        
