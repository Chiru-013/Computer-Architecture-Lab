	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
    load %x0, $n, %x3 ;x3 = n
    addi %x0, 0, %x4 ;i = 0
    addi %x0, 0, %x5 ;j = 0
loop:
    beq %x4, %x3, moveOut ;moveOut if i==n
    load %x4, $a, %x7 ;temp1 = a[i]
    load %x5, $a, %x8 ;temp2 = a[j]
    bgt %x8, %x7, swap ;compare
    jmp increement
swap:
    store %x8, $a, %x4
    store %x7, $a, %x5
    jmp increement
increement:
    addi %x5, 1, %x5 ;inreement j
    beq %x5, %x3, update
    jmp loop
update:
    addi %x4, 1, %x4 ;increement i
    addi %x4, 0, %x5 ;j = i
    jmp loop
moveOut:
    end