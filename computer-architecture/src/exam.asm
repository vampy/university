ASSUME CS: code, DS:data

; Read from the keyboard a number composed of multiple digits
; and do the sum of all the digits and write the result into a file

data SEGMENT
    filename DB 'sum.txt', 0
    filename_len = $-filename

    nr_max       DB  32 ; max length of the
    nr_length    DB  ? ; characters received
    nr           DB  32 DUP(?)

    sum DB 0
    sum_write DB ? ; the number of sums to pop out
    sum_temp DB ?

    n2 DB 2
    n10 DB 10
    n16 DB 16

    hex_table DB "0123456789ABCDEF"

    msg_give DB "Number: $"
    msg_error_open DB "Error opening file. $"
    msg_error_create DB "Error creating file. $"
    msg_error_write DB "Error writing to file. $"
data ENDS

code SEGMENT
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

start:
    mov AX, data
    mov DS, AX

    PRINT_STRING msg_give

    ; read string
    mov AH, 0Ah
    lea DX, nr_max
    int 21h

    PRINT_EOL

    ; compute the sum
    mov CL, nr_length
    mov CH, 0
    mov SI, 0
    compute_sum:
        mov AL, nr[SI]
        sub AL, '0' ; convert to normal number
        add sum, AL
        inc SI
        loop compute_sum

    ; push sum to stack
    mov AL, sum
    mov AH, 0
    mov sum_write, 0
    push_repeat:
        ; AX / ten, AH remainder, AL quotient
        div n16

        mov BL, AH
        mov BH, 0
        push BX

        ; prepare for next
        mov AH, 0
        add sum_write, 1

        cmp AL, 0
        jnz push_repeat

    ; create or empty file
    mov AH, 3Ch
    mov CX, 00h
    lea DX, filename
    int 21h

    jc create_error

    mov BX, AX ; save file handle for writing

    ; pop out and write to file in correct order
    ;mov AX, 0
    pop_repeat:
        POP AX

        ;save copy
        mov CX, BX

        ; use translation table for base 16
        lea BX, hex_table
        xlat

        ; AL our number move into memory
        mov sum_temp, AL

        ; write to file
        mov BX, CX ; recover file handle
        mov AH, 40h
        mov CX, 1
        lea DX, sum_temp
        int 21h

        jc write_error

        cmp sum_write, 0
        dec sum_write
        jnz pop_repeat

    jmp end_start
    open_error:
        PRINT_EOL
        PRINT_STRING msg_error_open
        jmp end_start

    jmp end_start
    create_error:
        PRINT_EOL
        PRINT_STRING msg_error_create
        jmp end_start

    jmp end_start
    write_error:
        PRINT_EOL
        PRINT_STRING msg_error_write
        jmp end_start


    end_start:
        PRINT_EOL
        mov AX, 4C00h ; function 4C with exit code 0
        int 21h
code ENDS
END start
