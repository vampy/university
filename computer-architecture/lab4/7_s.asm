ASSUME cs: code, ds:data

data SEGMENT
    a DB 20
    b DB 10
    c DW 5
    result DD ?
data ENDS

code SEGMENT
start:
    mov ax, data
    mov ds, ax
    ; .......
    ; a, b - byte
    ; c - word
    ; 7. (a-2)/(b+c)+a*c = 101

    ; DX:AX = AX * c
    mov AL, a
    cbw
    mov CX, AX
    imul c
    mov word PTR result, AX
    mov word PTR result + 2, DX
    ; result = a*c

    sub AX, 2
    mov CX, AX
    ; CX = (a - 2)

    mov AL, b
    cbw
    mov BX, AX
    add BX, c
    ; BX = (b+c)


    ; DX:AX / BX
    mov AX, CX
    ; AX = CX = (a - 2)
    cwd
    idiv BX
    mov BX, AX
    ; BX = (a-2)/(b+c)

    add word PTR result, BX
    adc word PTR result + 2, 0

    ;........
    mov ax, 4C00h
    int 21h                 ;finalul executiei programului aka exit()
code ENDS
END start
