	.data
a:
	10
	.text
main:
    load %x0, $a, %x4 ;no. is stored in x4
    add %x0, %x4, %x3 ;no. copied to x3 for arithematic operations
    add %x0, 0, %x10
    addi %x0, 10, %x30 ;immediate-10 saved in x30 regi
    blt %x4, %x0, notpalindrome ;if no. is negative
loop:
    div %x3, %x30, %x3 ;divide by 10 to extract the no. in ones place
    mul %x10, %x30, %x10 ;increment x10 regi by 10 
    add %x31, %x10, %x10 ;put the extracted no. in reverse order
    beq %x3, %x0, compare
    jmp loop
compare:
    beq %x4, %x10, ispalindrome
notpalindrome:
    subi %x0, 1, %x10
    end
ispalindrome:
    addi %x0, 1, %x10
    end