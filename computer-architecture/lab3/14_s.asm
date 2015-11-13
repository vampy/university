ASSUME cs: code, ds:data

data SEGMENT
    yy 	DB 	19 ; <the last two digits of your birth year (19yy)>
    d 	DB 	26 ; <your birth day (1-31)>
    result DW ?
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, ax
    ; .......
    ; (84+yy+yy)-(d+d) = 70

    ; 84 + yy + yy
    mov AL, yy ; AL = yy
    cbw        ; convert AL into signed AX
    mov BX, AX ; BX = AX = yy
    add BX, AX ; BX += AX  or BX =  yy + yy
    add BX, 84 ; BX += 84
    ; BX = (84+yy+yy)

    ; d + d
    mov AL, d ; AL = d
    cbw
    mov CX, AX ; CX = AX = d
    add CX, AX
    ; CX = (d +d)

    ; (84+yy+yy)-(d+d)
    sub BX, CX
    mov result, BX

    ;........
    mov AX, 4C00h
    int 21h         ;exit()
code ENDS
END start
