ASSUME CS: code, DS:data

;10. A string of bytes is given. Obtain the mirror image of the binary representation of this string of bytes.
;Ex: The byte string is given: s db 01011100b, 10001001b, 11100101b
;The result is the string: d db 10100111b, 10010001b, 00111010b. 

data SEGMENT
 x1 DB 01011100b
 x2 DB 10001001b
 x3 DB 11100101b 
 temp DB 0
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX            
    ; .......
    ;MOV AX, 65535

    mov AX, 0
    mov AL, x2
    mov AH, AL
    ; isolate low 4 bits
    and AL, 00001111b
    ; isolate high 4 bits
    and AH, 11110000b
    rol AL, 5
    ror AH, 5
    or temp, AL
    or temp, AH

    
    ;rol x1, 4
    
    ;rol x2, 4






    ;........
    mov AX, 4C00h
    int 21h
code ENDS
END start
