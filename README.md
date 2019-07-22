# ISO 8583 Swiss Army Knife

Tool to transcode and edit ISO 8583 files.

## Features

1. Parse ISO 8583 file and save content of messages into XML representation file

2. Load XML representation file and write valid ISO 8583 files

3. Parse and write ISO 8583 files with two encodings: ASCII and EBCDIC

4. Parse ISO 8583 files with four file-level layouts: Clean ISO 8583, RDW, Mastercard Pre-Edit, Mastercard Fixed1014

5. Write ISO 8583 files with three file-level layouts: Clean ISO 8583, RDW, Mastercard Fixed1014 (Pre-Edit files are not supported for writing)

6. Sets of encodings and file-level layouts can be extended to support any encodings and layouts you wish.

Only Mastercard IPM file structure is supported for now. But all another formats can be supported in easy way on demand. Feel free to ask in Issued.

## Some description about me and this tool
My name is Sergey Shakshin. I am Senior Software Engineer at Compass Plu Ltd.
Now I work on banking application platform TranzAxis. And this tool is made to help me and my colleagues to debug some specific situations and generate test cases for our platform.
You can use this code or compiled JAR to manipulate ISO 8583 files.
