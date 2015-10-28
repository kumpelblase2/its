void exploit(char *InputString) {
	char buf1[16] = "ABC\0";
	char buf2[16];

	//strcpy(buf1, InputString);
	strcpy(buf2, InputString);
	printf("%s\n", buf1);
	printf("%s", buf2);
}

void main(int argc, char *argv[]) {
	exploit(argv[1]);
}
