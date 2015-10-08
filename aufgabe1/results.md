## Server Headers
$ http --headers apache.org | grep Server
> Server: Apache/2.4.7 (Ubuntu)

CVEs:
https://httpd.apache.org/security/vulnerabilities_24.html

Tools: Wireshark

Header-Daten:
HTTP/1.1 302 Found
Date: Mon, 05 Oct 2015 20:14:39 GMT
Server: Apache/2.4.10 (Linux/SUSE)
Location: https://www.uni-hamburg.de/
Content-Length: 299
Content-Type: text/html; charset=iso-8859-1

Schwachstellen:
http://httpd.apache.org/security/vulnerabilities_24.html

## John the ripper
### Time

- 1 Characters (_): 0 seconds
- 2 Characters (d6): 0 seconds
- 3 Characters (_-$): 25 seconds?
- 4 Characters (_-$%): 3seconds
- 5 Characters (ilu32): 1min 8seconds

#### Incemental

58493057 - Time 8seconds

#### Wordlist

f9$äA43f - Time 0Seconds
