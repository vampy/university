ASSUME CS: code, DS:data

; 9. Being given a string of doublewords, build another string of doublewords
; which will include only the doublewords from the given string which have
; an even number of bits with the value 1.
; NOT WORKING

data SEGMENT
    s DD 12345678h, 1A2B3C4Dh, 0FE98DC76h
    s_len_bytes = $-s ; the lenght in bytes
    s_len_words = s_len_bytes/2 ; the length in words
    s_len_dwords = s_len_bytes/4 ; the length in double words

    d DD s_len_dwords DUP(0)
    zero DB 1
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX
    mov ES, AX

    ;mov AX, 0001101000101011b
    mov AX, 0ffffh
    add AX, 0

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

        add BX, 0
        jnp bothodd
            ; reset parity
            add zero, 0
            ; if here number of bits in BX is even
            ; check AX
            add AX, 0
            jnp endloop

        ; both AX and BX have even number of bits => even + even = even
        ; load into d
        jmp store

        bothodd:
            ; if here number of '1' bits in BX is odd
            ; check if AX is the same aka odd
            add AX, 0
            jp endloop
            ; if here both are odd => odd + odd = even
            ; load into d
            jmp store

        jmp endloop
        store:
            xchg AX, BX ; put low
            stosw
            mov AX, BX ; put high
            stosw
            jmp endloop

        endloop:
        loop repeat

    mov AX, 4C00h
    int 21h
code ENDS
END start
