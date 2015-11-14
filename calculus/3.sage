load common.sage

x_default_tuple = (x, -1, 1)
y_default_tuple = (y, -5, 5)

f(x, y) = x*e^y
f_diffx = f.diff(x) # first derivate with respect to x
f_diffy = f.diff(y) # first derivate with respect to y

f_diffx_diffx = f_diffx.diff(x) # second derivate of dx with respect to x
f_diffx_diffy = f_diffx.diff(y) # second derivate of dx with respect to y

f_diffy_diffx = f_diffy.diff(x) # second derivate of dy with respect to x
f_diffy_diffy = f_diffy.diff(y) # second derivate of dy with respect to y


T1 = get_t1(1, 0)
T2 = get_t2(1, 0)

# all plots variables
f_plot = plot3d(f, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme())
f_diffx_plot = plot3d(f_diffx, x_default_tuple, y_default_tuple)
f_diffy_plot = plot3d(f_diffy, x_default_tuple, y_default_tuple)

T1_plot = plot3d(T1, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme("coolwarm"))
T2_plot = plot3d(T2, x_default_tuple, y_default_tuple, adaptive=True, color=color_scheme("coolwarm"))
