; 10. A string of bytes is given. Obtain the mirror image of the binary representation of this string of bytes.
; Ex: The byte string is given: s db 01011100b, 10001001b, 11100101b
; The result is the string: d db 10100111b, 10010001b, 00111010b.

assume cs:code, ds:data
data segment
    s db 01011100b, 10001001b, 11100101b
    leng equ $-s
    d db leng dup (?)
    s1 db 10100111b
    s2 db 10010001b
    s3 db 00111010b
data ends
code segment
start:
    mov ax, data
    mov ds, ax
    mov es, ax

    lea si, s+2
    lea di, d

    mov ch, leng-1

    mov bx, 0
    repeat:
        mov ah, 00000001b
        std
        lodsb
        mov dl, 0
        mov cl, 1
        loop1:
                mov bl, al
                and bl,ah
                ror bl, cl
                or dl, bl
                shl ah, 1
                add cl, 2
                cmp cl, 17
        jnz loop1
        mov al, dl
        cld
        stosb
        sub ch, 1
        jns repeat

    mov ax, 4c00h
    int 21h
code ends
end start
