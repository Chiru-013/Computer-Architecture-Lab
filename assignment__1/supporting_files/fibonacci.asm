	.data
n:
	12
	.text
main:
	load %x0, $n, %x30
	addi %x0, 0, %x4
	addi %x4, 1, %x5
	addi %x4, 1, %x3
	addi %x3, 1, %x31
	addi %x0, 65535, %x20 ;x20 points to 65535 memory loaction
	store %x4, 0, %x20 ;store '0' in 65535 location
	blt %x30, %x31, moveOut ;if n =1 prints only 0
	subi %x20, 1, %x20 ;subtract to move to lower memory location
	store %x5, 0, %x20 ;store '1' in 64434 memo location
	addi %x3, 1, %x3
loop:
	beq %x3, %x30, moveOut
	add %x4, %x5, %x6 ;add previous two nos. to get next no.
	subi %x20, 1, %x20
	store %x6, 0, %x20
	addi %x5, 0, %x4
	addi %x6, 0, %x5
	addi %x3, 1, %x3
	jmp loop
moveOut:
	end