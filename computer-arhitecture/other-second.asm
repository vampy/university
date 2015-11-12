; Se da un sir de N cuvinte
; Sa se tipareasca in baza 16 catul si restul impartii fara semn A/B
; unde A este maximul valorilor octetilor inferiori ai sirului de cuvinte date
; iar B este minimul valorilor octetilor superiori ai sirului de cuvinte date
; Ex: sir_cuv DW 21520, -6, "xy", 0f5b2h, -129
; A = 194 si B = 10
; Se va printa: catul=13h si restul=04h
ASSUME CS:code, DS:data

data SEGMENT
    sir DW 21520, -6, "xy", 0f5b2h, -129
    sir_lenb = $-sir
    sir_lenw = sir_lenb/2 ; the length in words
    
    n16 DB 16
    
    max_sir DB ?
    min_sir DB ?
   
    print_q DB 0
    print_r DB 0 
    
    msg_q DB "Catul: $"
    msg_r DB "Restul: $"
    
    ; used for xlat
    hex_table DB "012345678ABCDEF"
    
    CRLF DB 13, 10, '$'
data ENDS

code SEGMENT
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

    PRINT_EOL MACRO
        PRINT_STRING CRLF
    ENDM
    
    print_nr16 PROC ; AL contains the number that we want to print
        push BX
        push CX
        push DX
        ; put all the digits in proper form on the stack
        
        mov CX, 0
        print_get_digits:
            mov AH, 0
            div n16 ; AL - quotient, AH - remainder
            
            ; save the result
            mov DX, AX
            
            ; convert the remainder
            mov AL, AH
            lea BX, hex_table
            xlat
            
            ; AL contains the proper ascii code
            mov AH, 0
            push AX
            
            ; restore AX
            mov AX, DX
            
            inc CX
            cmp AL, 0 ; unil the quotient is 0
            jnz print_get_digits
        
        ; CX contains the proper number of prints
        print_digits:
            pop AX
            PRINT_CHAR AL
            loop print_digits
            
        pop DX
        pop CX
        pop BX
        ret
    print_nr16 ENDP
start:
    mov AX, data
    mov DS, AX
    
    lea SI, sir
    ; init max and minim
    lodsb ; AL has the lower byte
    mov max_sir, AL
    lodsb ; AL has the higher byte
    mov min_sir, AL
    
    ; find the rest in the array
    mov CX, sir_lenw
    dec CX ; because we handled the first word
    find_loop:
        lodsb ; the lower byte in AL
        ; find out if maximum
        cmp AL, max_sir
        ja found_max
        end_cmp_max:
         
        lodsb ; the higher byte is in AL 
        ; find out the minimum
        cmp AL,  min_sir
        jb found_min
        
        jmp end_find_loop
        found_min:
            mov min_sir, AL
            jmp end_find_loop
         
        jmp end_find_loop
        found_max:
            mov max_sir, AL
            jmp end_cmp_max ; we found a maximum go up to find the minimum
        
        end_find_loop:
        
        loop find_loop
    
    ; A = max_sir, B = min_sir
    mov AL, max_sir
    mov AH, 0
    div min_sir
    ; quotient=AL, remainder = AH
    mov print_q, AL
    mov print_r, AH
    
    mov AL, print_q
    mov AH, 0
    
    PRINT_STRING msg_q
    call print_nr16
    
    PRINT_EOL
    
    mov AL, print_r
    mov AH, 0
    
    PRINT_STRING msg_r
    call print_nr16
    
    end_start:
    
        mov AX, 4C00h
        int 21h
code ENDS
END start
