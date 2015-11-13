ASSUME CS: code, DS:data

data SEGMENT    
    n DB 'Butum Daniel', '$'
    n_len = $-n
    
    nr DB 255
    ten DB 10
data ENDS

code SEGMENT
    PRINT_NEWLINE MACRO
        mov DL, 10
        mov AH, 02h
        int 21h
    ENDM
    
    PRINT_STRING MACRO string
        lea DX, string
        mov AH, 09h
        int 21h
    ENDM
    
    PRINT_CHAR MACRO char
        mov DL, char
        mov AH, 02h
        int 21h
    ENDM

start:
    mov AX, data
    mov DS, AX          
    ; .......
    
    PRINT_STRING n
    
    mov AL, nr
    mov AH, 0
    mov CX, 0
    repeat:
        ; AX / ten
        div ten
        ; AL the quotient, AH the remainder
        mov BL, AH
        mov BH, 0
        ; save the digit to the stack
        push BX
        
        ; prepare for next iteration
        mov AH, 0
        inc CX ; the number of digits
        
        cmp AL, 0
        jnz repeat
        
    repeat_display:
        pop AX
        add AL, '0'
        PRINT_CHAR AL
        loop repeat_display
    
    PRINT_NEWLINE
    
    ; because of end '$' and len is bigger than 1
    mov SI, n_len - 2
    repeat:
        PRINT_CHAR n[SI]
        dec SI
        cmp SI, 0
        jnz repeat
    
    ; last character
    PRINT_CHAR n[SI]
    
    PRINT_NEWLINE
    
    ; normal
    PRINT_STRING n
    
    ;........
    mov AH, 4ch
    mov AL, 00h
    int 21h
code ENDS
END start
