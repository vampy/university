x, y = var("x, y")
x_default_tuple = (x, -1, 1)
y_default_tuple = (y, -1, 1)

def color_scheme(color="autumn"):
    return [colormaps[color](i) for i in sxrange(0, 1, 0.05)]

def get_t1(a=0, b=0):
    return f(a, b) + f_diffx(a, b)*(x - a) + f_diffy(a, b)*(y - b)

def get_t2(a=0, b=0):
    return f(a, b) + f_diffx(a, b)*(x - a) + f_diffy(a, b)*(y - b) + 0.5*f_diffx_diffx(a, b)*(x - a)^2 + f_diffy_diffx(a, b)*(x - a)*(y - b) + 0.5*f_diffy_diffy(a, b)*(y - b)^2
 

