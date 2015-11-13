ASSUME ds:data, cs: code

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
    ; .......
    ; (yy+yy+yy)-h+(d-m) = (actual) -109

    mov AX, 3
    add AX, AX
    add AX, AX

    ;(yy+yy+yy)
    mov AL, yy ; AX = yy
    cbw
    mov BX, AX
    add BX, AX
    add BX, AX
    ; BX = (yy+yy+yy)

    ; (yy+yy+yy)-h = -57
    mov AL, h
    cbw  ; AX = -70
    ; AX is negative add
    sub BX, AX
    ;add BX, AX
    ; BX = -128

    ;(d-m)
    mov AL, d
    cbw

    mov CX, AX

    mov AL, m
    cbw

    sub CX, AX
    ;CX = (d-m)

    ; ((yy+yy+yy)-h)+(d-m)
    add BX, CX
    mov result, CX

    ;........
    mov ax, 4C00h
    int 21h
code ENDS
END start
