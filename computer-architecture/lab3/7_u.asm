ASSUME cs: code, ds:data

data SEGMENT
    yy 	DB 	19 ; <the last two digits of your birth year (19yy)>
    d 	DB 	26 ; <your birth day (1-31)>
    h   DB 185 ; <your height in cm (100-300)>
    m   DB 7   ; <the month you were born (1-12)>
    result DW ?
data ENDS

code SEGMENT
start:
    mov ax, data
    mov ds, ax
    ; (yy + yy + yy) - h + (d - m)

    ;(yy + yy + yy)
    mov AH, 0
    mov AL, yy ; AX = yy
    mov BX, AX
    add BX, AX
    add BX, AX
    ; BX = (yy + yy + yy)

    ; (yy + yy + yy) - h
    mov AH, 0
    mov AL, h
    ; AX = 185
    sub BX, AX
    ; BX = (yy + yy + yy) - h = -128
    mov BH, 0 ; BX = 128

    ;(d - m)
    mov AH, 0
    mov AL, d
    ; AX = d
    mov CH, 0
    mov CL, m
    ; CX = m
    sub AX, CX
    ; AX = (d - m) = 19

    ; ((yy + yy + yy) - h) + (d - m)
    add BX, AX
    mov result, BX
    ; result = 147

    mov ax, 4C00h
    int 21h
code ENDS
END start
