void exploit(char *InputString) {
	char buf1[16];
	char buf2[16];

	strcpy(buf1, InputString);
	strcpy(buf2, &InputString[16]);
	printf("%s\n", buf1);
	printf("%s", buf2);
}

void main(int argc, char *argv[]) {
	exploit(argv[1]);
}
