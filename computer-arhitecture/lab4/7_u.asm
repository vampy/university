 ASSUME cs: code, ds:data

data SEGMENT                
    a DB 20
    b DB 10
    c DW 5
    result DD ?  
data ENDS
 
code SEGMENT                
start:
    mov AX, data             
    mov DS, AX
    ; .......
    ; a, b - byte
    ; c - word
    ; (a-2)/(b+c)+a*c = 101

    ; DX:AX = AX * c
    mov AL, a
    mov AH, 0
    mul c
    mov word PTR result, AX
    mov word PTR result + 2, DX
    ; result = a*c
    
    mov AL, a
    mov AH, 0
    sub AX, 2
    ; AX = (a - 2)
    
    mov BL, b
    mov BH, 0
    add BX, c
    ; BX = (b+c)
    
    ; DX:AX / BX
    mov DX, 0
    div BX
    mov BX, AX
    ; BX = (a-2)/(b+c)
    
    add word PTR result, BX
    adc word PTR result + 2, 0
    
    ;........
    mov AX, 4C00h
    int 21h                 ;finalul executiei programului aka exit()
code ENDS
END start
