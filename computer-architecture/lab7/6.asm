
assume cs:code, ds:data
data segment
      sir DW 12345, 678, 912, 3241
      len EQU ($-sir)/2 ; len in words
      zec dw 10
      rez db len*5 dup (?) ; result; FFFFh is 32676 => 5 maxin digits in a word => rez = len * 5
data ends

code segment
start:
      mov ax, data
      mov ds, ax

      mov si, offset sir + len*2 - 2
      mov ax, seg sir
      mov ds, ax ; ds:si - string

      mov di, offset rez + len*5 - 1
      mov ax, seg rez
      mov es, ax ; es:di - rezultatul

      std

      mov cx, len
      repeta:
          lodsw ; ax = current word
          mov dx, 0

         Imparte:
            div zec ; dx - remainder (digit), ax - quotient
            mov bx, ax
            mov ax, dx
            add ax, 48
            stosb
            mov ax, bx
            mov dx, 0
            cmp ax, 0
            jne Imparte

         loop repeta

      mov ax, 4C00h
      int 21h
code ends
end start
