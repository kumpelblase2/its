/*
  generate unix password line containing from a username and password
 */


#include <stdlib.h>

#include <unistd.h>
#include <crypt.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>

void usage(char* prg)
{
  fprintf(stderr,"usage: %s [-u USERNAME][-s SALT] PASSWORD\n\tprints a valid UNIX/Linux passwd line for a user\n",prg);
  fprintf(stderr,"      -s SALT      use SALT as the salt for password encyption. Default are two\n");
  fprintf(stderr,"                     random characters. Only the first two characters are used.\n");
  fprintf(stderr,"                     If the characters are not element of [a-zA-Z0-9./] the byte\n");
  fprintf(stderr,"                     value modulo 64 is used instead.\n");
  fprintf(stderr,"      -u USERNAME  Set USERNAME as the username for the generated line. Default\n                     is 'testar_a'.\n");
  return;
}

int main(int argc, char **argv)
{
  char *salt_valid="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./";

  unsigned char salt[2];
  char *user="testar_a", *pass, *cpass, *salt_pre=NULL;
  FILE* ran_f;
  int     c, errflg = 0;

  optarg = NULL;
  while (!errflg && ((c = getopt(argc, argv, "u:s:")) != -1))
    switch (c) {
    case 'u'        :
      user = optarg;
      break;
    case 's'        :
      salt_pre= optarg;
      break;
    default :
      errflg++;
    }
  if((errflg) ||(optind >= argc))
    {
      usage(argv[0]);
      exit(1);
    }
  pass=argv[optind];

  if(!salt_pre)
    {
      if(!(ran_f=fopen("/dev/urandom","r")))
	{
	  perror("could not open file /dev/urandom");
	  exit(1);
	}

      if(!fread(salt,2,1,ran_f))
	{
	  perror("could not read 2 Bytes from /dev/urandom");
	  exit(1);
	}
      fclose(ran_f);
      salt[0]=salt_valid[salt[0]%64];
      salt[1]=salt_valid[salt[1]%64];
    }
  else
    {
      int i;

      if(strlen(salt_pre)<2)
	{
	  fprintf(stderr,"Not enough salt characters. 2 required\n");
	  exit(1);
	}

      for(i=0;i<2;i++)
	{
	  if(isalnum(salt_pre[i])||(salt_pre[i]=='.')||(salt_pre[i]=='/'))
	    salt[i]=salt_pre[i];
	  else
	    salt[i]=salt_valid[salt_pre[i]%64];
	}
    }


  cpass=crypt(pass,salt);
  fprintf(stdout,"%s:%.13s:4711:Alessandro Testarossa:/some/file/system:/the/ultimate/shell\n",user,cpass);

  exit(0);
}
