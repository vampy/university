ASSUME cs: code, ds:data

;7. A byte string S is given. Obtain the string D1 which contains the 
;   elements found on the even positions of S and the string D2 which 
;   contains the elements found on the odd positions of S.

;   Exemple:
;   S: 1, 5, 3, 8, 2, 9
;   D1: 1, 3, 2
;   D2: 5, 8, 9

data SEGMENT
    s DB 1, 5, 3, 8, 2, 9
    s_len EQU $-s
    d_len EQU s_len/2
    
    ; handle odd length
    d1 DB d_len + 1 DUP(?)
    d2 DB d_len DUP(?)
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX             
    ; .......
    mov SI, 0 ; index for s
    mov DI, 0 ; index for d1 and d2
    
    repeat:
        ; move the even position into d1
        mov AL, s[SI]
        mov d1[DI], AL
        
        ; move the odd position into d2
        mov BL, s[SI + 1]
        mov d2[DI], BL
        
        ; increment the indexes
        add SI, 2
        inc DI
        
        cmp DI, d_len
        jb repeat
    
    ; handle odd length
    mov AL, s_len
    shr AL, 1
    ; check if even or odd length
    jnc end_program
        mov AL, s[SI]
        mov d1[DI], AL
        
    
    end_program:
    ;........
    mov AX, 4C00h
    int 21h
code ENDS
END start
