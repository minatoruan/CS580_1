import socket
import sys

socket.socket(socket.AF_INET, socket.SOCK_DGRAM).sendto('!q', ('localhost', 9002))

    
