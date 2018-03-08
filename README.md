# OLT_Port
OLT Port Boot

Algorithm that aims inspect Optical Line Termination (OLT) to count available PON ports.

Steps:

<br> 1 - Read a text file (input file) <OLT_IP;OLT_Name> and outputs OLT_name.txt that contains numbering of used and available PON ports.
<br> 2 - For each element <IP> connect via SSH.
<br> 3 - Run the command <display board 0/x> x is the phisical board. It can be 0 until x boards.
<br> 4 - Catch the SSH output and save in text file.
