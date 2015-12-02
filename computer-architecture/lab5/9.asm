ASSUME cs: code, ds:data

; 9. The word A and the byte B are given. Obtain the byte C in the following way:
;  - the bits 0-3 of C are the same as the bits 6-9 of A
;  - the bits 4-5 of C have the value 1
;  - the bits 6-7 of C are the same as the bits 1-2 of B

data SEGMENT
    a dw 0111011101010111b ; 0111 0111 0101 0111
    b db 10111110b
    c DB 0 ; 0000 0000b
    ; c final should be  11 11 1101b = 253
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX

    mov AX, a ; AX = a = 0111 0111 0101 0111b
    shr AX, 6 ; AX = 000 0001 1101 1101b = 1DDh
    ; we work now with AL = 1101 1101b
    ; bits 6-9 of AX are now bits 0-3 of AL
    ; isolate bits 0-3
    and AL, 00001111b
    ; put the bits 0-3 on c
    or c, AL

    ; we put 1 on bits 4-5 of c
    or c, 00110000b

    mov AL, b ; AL = 1011 1110b
    shl AL, 5 ; AL = 1100 0000b
    ; bits 1-2 of AL are now in positions 6-7
    ; isolate bits 6-7
    and AL, 11000000b
    ; put onto c
    or c, AL

    mov AX, 4C00h
    int 21h
code ENDS
END start
