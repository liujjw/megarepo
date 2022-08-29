	.file	"testi2.c"
	.text
	.globl	main
	.align	16, 0x90
	.type	main,@function
main:                                   # @main
# BB#0:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$8, %esp
	leal	.L.str, %eax
	movl	%eax, (%esp)
	calll	printf
	movl	$0, %ecx
	movl	%eax, -4(%ebp)          # 4-byte Spill
	movl	%ecx, %eax
	addl	$8, %esp
	popl	%ebp
	ret
.Ltmp0:
	.size	main, .Ltmp0-main

	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"Hello, world!\n"
	.size	.L.str, 15


	.ident	"Ubuntu clang version 3.4-1ubuntu3 (tags/RELEASE_34/final) (based on LLVM 3.4)"
	.section	".note.GNU-stack","",@progbits
