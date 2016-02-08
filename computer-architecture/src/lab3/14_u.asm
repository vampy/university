ASSUME cs: code, ds:data

data SEGMENT
    yy 	DB 	19 ; <the last two digits of your birth year (19yy)>
    d 	DB 	26 ; <your birth day (1-31)>
    negative DB -3
    result DW ?
data ENDS

code SEGMENT
start:
    mov ax, data             ; the address of the data segment is copied into AX
    mov ds, ax               ; copy AX into DX
    ; (84 + yy + yy) - (d + d) = 70

    ; 84 + yy + yy
    mov AH, 0
    mov AL, yy ; AL = yy
    mov BX, AX ; BX = AX
    add BX, AX ; BX += yy
    add BX, 84 ; BX += 84
    ; BX = (84 + yy + yy)

    ; d + d
    mov AH, 0
    mov AL, d ; BL = d
    add AX, AX
    ; AX = d + d

    ; (84 + yy + yy) - (d + d)
    sub BX, AX
    ; mov AH, 0
    mov result, BX

    mov ax, 4C00h
    int 21h
code ENDS
END start
