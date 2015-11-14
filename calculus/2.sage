load common.sage

f(x, y) = e^(-x^2 -y^2)
f_diffx = f.diff(x) # first derivate with respect to x
f_diffy = f.diff(y) # first derivate with respect to y

f_diffx_diffx = f_diffx.diff(x) # second derivate of dx with respect to x
f_diffx_diffy = f_diffx.diff(y) # second derivate of dx with respect to y

f_diffy_diffx = f_diffy.diff(x) # second derivate of dy with respect to x
f_diffy_diffy = f_diffy.diff(y) # second derivate of dy with respect to y


# (a, b) = (0, 0)
#T1(x, y) = f(0, 0) + f_diffx(0, 0) * x + f_diffy(0, 0) * y
#T2(x, y) = f(0, 0) + f_diffx(0, 0)*x + f_diffy(0, 0)*y +  0.5*f_diffx_diffx(0, 0) * x^2 + f_diffy_diffx(0, 0)*x*y + 0.5*f_diffy_diffy(0, 0)*y^2

T1(x, y) = get_t1(0, 0)
T2(x, y) = get_t2(0, 0)

# all plots variables
f_plot = plot3d(f, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme())
f_diffx_plot = plot3d(f_diffx, x_default_tuple, y_default_tuple)
f_diffy_plot = plot3d(f_diffy, x_default_tuple, y_default_tuple)

f_diffx_diffx_plot = plot3d(f_diffx_diffx, x_default_tuple, y_default_tuple)
f_diffx_diffy_plot = plot3d(f_diffx_diffy, x_default_tuple, y_default_tuple)
f_diffy_diffx_plot = plot3d(f_diffy_diffx, x_default_tuple, y_default_tuple)
f_diffy_diffy_plot = plot3d(f_diffy_diffy, x_default_tuple, y_default_tuple)

T1_plot = plot3d(T1, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme("coolwarm"))
T2_plot = plot3d(T2, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme("coolwarm"))
