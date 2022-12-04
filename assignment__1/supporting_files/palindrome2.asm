	.data
a:
	101
	.text
main:
	load %x0, $a, %x4
	add %x0, %x4, %x3
	add %x0, %x0, %x30
	blt %x4, %x0, notpalindrome
loop:
	divi %x3, 10, %x3
	muli %x30, 10, %x30
	add %x30, %x31, %x30
	beq %x0, %x3, compare
	jmp loop
compare:
	beq %x4, %x30, ispalindrome
notpalindrome:
	subi %x0, 1, %x10
	end
ispalindrome:
	addi %x0, 1, %x10
	end