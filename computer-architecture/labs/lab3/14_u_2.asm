ASSUME cs: code, ds:data     ; tell the assembler the segments used by us

data SEGMENT
    yy 	DB 	19 ; <the last two digits of your birth year (19yy)>
    d 	DB 	26 ; <your birth day (1-31)>
    result DB ?
data ENDS

code SEGMENT
start:
    mov ax,data             ; the address of the data segment is copied into AX
    mov ds,ax               ; copy AX into DX
    ; (84 + yy + yy) - (d + d)

    ; 84 + yy + yy
    mov AH, 0
    mov AL, 84 ; AL = 64
    add AL, yy ; AL += yy
    add AL, yy ; AL += yy

    ; d + d
    mov BH, 0
    mov BL, d ; BL = d
    add BL, d  ; BL += d aka BL = 2*d

    ; (84 + yy + yy) - (d + d)
    sub AL, BL
    mov result, AL

    mov ax, 4C00h
    int 21h
code ENDS
END start
