#include <stdio.h>
#include <string.h>

main() {
	char line[255], *userline, *passline, *s;
	char user[20], pass[20];

	printf("Content-Type: text/html\n\n");
	printf("<html><head></head>");
	printf("<body>");
	fgets(line, 255, stdin);
	printf("Parameters are: <br />");
	
	userline = strtok(line, "&");
	passline = strtok(0, "&");

	user[0] = 0;
	if (userline) {
		s = strtok(userline, "=");
		s = strtok(0, "=");
		if (s) strcpy(user, s);
	}

	pass[0] = 0;
	if (passline) {
		s = strtok(passline, "=");
		s = strtok(0, "=");
		if (s) strcpy(pass, s);
	}
	printf("%s, %s", user, pass);

	printf("</body>");
	printf("</html>");
}
