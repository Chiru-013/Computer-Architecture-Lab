	.data
a:
	12
d:
	2
n:
    5
	.text
main:
    load %x0, $n, %x3
    load %x0, $d, %x4
    addi %x0, 65535, %x1
    load %x0, $a, %x5
    addi %x0, 0, %x6
loop:
    mul %x6, %x4, %x7
    add %x7, %x5, %x10
    store %x10, 0, %x1
    subi %x1, 1, %x1
    addi %x6, 1, %x6
    bne %x6, %x3, loop
    end
