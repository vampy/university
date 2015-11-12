ASSUME CS: code, DS:data

; 9. Being given a string of doublewords, build another string of doublewords 
; which will include only the doublewords from the given string which have 
; an even number of bits with the value 1. 

data SEGMENT    
    s DD 12345678h, 1A2B3C4Dh, 0FE98DC76h
    s_len_bytes = $-s ; the lenght in bytes
    s_len_words = s_len_bytes/2 ; the length in words
    s_len_dwords = s_len_bytes/4 ; the length in double words
    
    d DD s_len_dwords DUP(0)
    nrb DB 0
    temp1 DW 0
    temp2 DW 0
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX    
    mov ES, AX        
    ; .......
    
    ; load addreses
    lea SI, s
    lea DI, d
    
    ; set length 
    mov CX, s_len_dwords
    
    ; clear direction flag parse from left to right
    cld
    
    repeat:
        lodsw ; load the least significant word
        mov BX, AX
        lodsw ; load the high significant word
        ; we have our number in AX:BX
        
        ; count the number of bits
        mov DX, 16
        mov temp1, BX
        mov temp2, AX
        repeat_nrb:
            ; calculate BX
            shr temp1, 1
            jnc end_nrb1
                inc nrb
            end_nrb1:
            
            ; calculate the number of bits AX
            shr temp2, 1
            jnc end_nrb2
                inc nrb
            end_nrb2:
            
            end_repeat_nrb:
            dec DX
            cmp DX, 0
            jnz repeat_nrb
        
        ; sum
        shr nrb, 1
        ; jump if even number of bits
        jnc store    
        
        jmp endloop
        store:
            xchg AX, BX ; put low
            stosw 
            mov AX, BX ; put high
            stosw
            jmp endloop

        endloop:
        ; reset nrb
        mov nrb, 0
        loop repeat

    
    ;........
    mov AX, 4C00h
    int 21h
code ENDS
END start
