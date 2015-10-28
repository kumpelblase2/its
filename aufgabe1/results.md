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
`$ john --incremental=digits`
58493057 - Time 8seconds

#### Wordlist
`$ john --wordlist=wordlist.lst`
f9$äA43f - Time 0Seconds

## Phishing
Website: Postbank Online-Banking

id3_hf_0=
jsDisabled=false
nutzername=32573465
kennwort=tet123
loginButton=Anmelden

Notiz: Der Link für das "Action" in der PBCopy.html Datei wurde durch "login.php" ersetzt, um die Daten zu erhalten. In dem Projekt wurden die .css Files und Bilder nicht hinzugefügt.


## Buffer Overflow

```
$ ./bufferoverflow "DDDDDDDDDDDDDDDD    "

DDDDDDDDDDDDDDDD    %
```

```
$ ./bufferoverflow DDDDDDDDDDDDDDDDDDDDDDDDD
DDDDDDDDD
DDDDDDDDDDDDDDDDDDDDDDDDD%
```

```
$ ./bufferoverflow DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
[1]    19154 segmentation fault (core dumped)  ./bufferoverflow DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD

```
