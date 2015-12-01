ASSUME CS: code, DS:data

; 8. Print of the screen, for each number between 32 and 126, the value
; of the number (in base 10) and the character whose ASCII code the number is.

data SEGMENT public
    msg_char_dec DB '  DEC: ', '$'
    msg_char_ascii DB ' ASCII: ', '$'
    ;sep DB " || ", '$'
    char_to_print DB 0
    n10 DB 10
data ENDS


code SEGMENT public
    public print

    PRINT_EOL MACRO
        mov DL, 13 ; \r
        mov AH, 02h
        int 21h
        mov DL, 10 ; \n
        mov AH, 02h
        int 21h
        ; print \r\n
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

    READ_STRING MACRO max_bytes
        mov AH, 0Ah
        lea DX, max_bytes
        int 21h
        ; Afet IHR will be executred the name of the file will be stored
        ; at max_bytes + 2 and the offset max_bytes + 1 will store the length
    ENDM

    ; WORKS only with characters read from the stdin because the last
    ; character is /r/n and the length is only the read characters
    ; without the /r/n character
    ; for strings defined in the data segment you must
    ; decrement the length by one
    CONVERT_TO_ASCIIZ MACRO string, string_length
        ; Assume string is always longer with 1
        mov AL, string_length
        mov AH, 0
        mov SI, AX
        mov string[SI], 0
    ENDM


    ; BX will be altered and also AL
    ; return:
    ;       in AL
    ;hexnum_to_char PROC
    ;    lea BX, hex_table
    ;    xlat
    ;    ret
    ;hexnum_to_char ENDP

    print:
        ; save the state of the registers
        push AX
        push BX
        push CX
        push DX

        ; save the char
        mov char_to_print, AL

        ;PRINT_STRING sep
        PRINT_EOL
        PRINT_STRING msg_char_ascii
        PRINT_CHAR char_to_print

        PRINT_STRING msg_char_dec
        mov AH, 0
        mov AL, char_to_print
        mov CX, 0
        push_repeat:
            ; AX / ten
            div n10

            mov BH, 0
            mov BL, AH ; save the remainder
            push BX

            ; prepare for next
            mov AH, 0
            inc CX

            cmp AL, 0
            jnz push_repeat

        pop_repeat:
            POP AX
            add AL, '0'
            PRINT_CHAR AL
            loop pop_repeat

        ; restore state
        pop DX
        pop CX
        pop BX
        pop AX
        ret

code ENDS
end
