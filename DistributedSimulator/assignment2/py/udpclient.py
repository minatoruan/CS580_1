import socket
import sys
import time
HOST, PORT = 'localhost', 9001
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
"""
while (True):
    word = raw_input('Enter your word (leave blank for exit): ')
    if word == '': break
    else:
        sock.sendto(word, (HOST, PORT))
        len_msg = sock.recv(1024)
        definition = sock.recv(int(len_msg))
        print "Definition: ", definition
"""
class Timer(object):
    def __init__(self, verbose=False):
        self.verbose = verbose

    def __enter__(self):
        self.start = time.time()
        return self

    def __exit__(self, *args):
        self.end = time.time()
        self.secs = self.end - self.start
        self.msecs = self.secs * 1000  # millisecs
        if self.verbose:
            print 'elapsed time: %f ms' % self.msecs
            
sentence = 'After the signatures are folded and gathered they move into the bindery In the middle of last century there were still many trade binders standalone binding companies which did no printing specializing in binding alone At that time because of the dominance of letterpress printing typesetting and printing took place in one location and binding in a different factory When type was all metal a typical worth of book worth of type would be bulky fragile and heavy The less it was moved in this condition the better so printing would be carried out in the same location as the typesetting Printed sheets on the other hand could easily be moved Now because of increasing computerization of preparing a book for the printer the typesetting part of the job has flowed upstream where it is done either by separately contracting companies working for the publisher by the publishers themselves or even by the authors Mergers in the book manufacturing industry mean that it is now unusual to find a bindery which is not also involved in book printing and vice versa'

with Timer() as t:
    for word in sentence.split():
        sock.sendto(word, (HOST, PORT))
        len_msg = sock.recv(1024)
        definition = sock.recv(int(len_msg))
        print "Definition: ", definition
print "=> elasped lpush: %s s" % t.secs

