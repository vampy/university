ASSUME CS: code, DS:data

; 11. A string of doublewords is given. Compute the string formed by the
; high bytes of the low words from the elements of the doubleword string
; and these bytes should be multiple of 10.
; Ex: Being given the string: s dd 1234 5678h, 1A2B 3C4Dh, FE98 DC76h
; The result is the string: d db 3Ch, DCh.

data SEGMENT
    s DD 12345678h, 1A2B3C4Dh, 0FE98DC76h
    s_len_bytes = $-s ; the lenght in bytes
    s_len_words = s_len_bytes/2 ; the length in words
    s_len_dwords = s_len_bytes/4 ; the length in double words

    d DB s_len_dwords DUP(0)
    ten DB 10
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX
    mov ES, AX

    ; load addreses
    lea SI, s
    lea DI, d

    ; set length
    mov CX, s_len_dwords

    ; clear direction flag parse from left to right
    cld

    repeat:
        lodsb ; load the least significant byte of the low word
        lodsb ; load the high significant byte of the low word
        ; we have our byte in AL

        ; make copy
        mov BL, AL
        mov AH, 0
        div ten
        ; check if multiple of 10
        cmp AH, 0
        jnz nonmultiple
            mov AL, BL
            stosb
        nonmultiple:

        lodsw ; we are not intrested in the high word go forward
        loop repeat

    ;........
    mov AX, 4C00h
    int 21h
code ENDS
END start
