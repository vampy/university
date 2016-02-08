ASSUME CS:code, DS:data

data SEGMENT
    CRLF DB 13, 10, '$'

    read_max DB 25
    read_length DB ?
    read_string DB 25 DUP(?)

    filename DB "test.in", 0
    filehandle DW ?
    filebuffer DB 20 DUP(?)

    msg_error DB "ERROR OPENING", '$'
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

start:
    mov AX, data
    mov DS, AX

    mov AH, 3Dh
    mov AL, 0  ; open file for reading
    lea DX, filename
    int 21h

    jc open_error ; jump if error
    mov filehandle, AX ; save the file handle

    lea DX, filebuffer ; buffer
    mov CX, 2 ; length of read
    mov BX, filehandle ; file handle
    mov AH, 3Fh
    int 21h

    jmp end_start
    open_error:
        PRINT_STRING msg_error
        jmp end_start

    end_start:
        mov AX, 4C00h
        int 21h
code ENDS
END start
